package org.jetbrains.research.ml.dataset.anonymizer

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import krangl.DataFrame
import krangl.map
import krangl.readCSV
import krangl.writeCSV
import org.jetbrains.research.ml.dataset.anonymizer.util.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getTmpProjectDir(): String {
    val path = "${System.getProperty("java.io.tmpdir")}/tmpProject"
    createFolder(path)
    return path
}

abstract class Anonymizer(private val tmpDataPath: String) {
    protected abstract val language: Language
    protected abstract val transformations: List<(PsiElement, Boolean) -> Unit>
    protected abstract val project: Project
    private var counter: Int = 0

    protected fun anonymize(code: String): PsiElement {
        val file = createFile("$tmpDataPath/tmp_${counter}${language.extension.value}", code)
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
            ?: error("The virtual file ${file.path} was not created")
        val psi = ApplicationManager.getApplication().runReadAction<PsiElement> {
            PsiManager.getInstance(project).findFile(virtualFile) as PsiElement
        }
        ApplicationManager.getApplication().invokeAndWait {
            transformations.forEach { it(psi, false) }
            ApplicationManager.getApplication().runWriteAction {
                FileUtil.delete(file)
            }
        }
        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
        counter++
        return psi
    }

    /**
     * The root folder need to have the following structure:
     * -root
     * --language
     * ---taskN1
     * ----csv files
     * ---taskN2
     * ----csv files
     * ...
     */
    fun anonymizeLanguageFolder(rootPath: String) {
        val root = File(rootPath)
        val outputLanguageFolder = "${root.parent}/anonymizerResult/${language.value}"
        val languageFolder = File("$rootPath/${language.value}")
        languageFolder.listFiles()?.filter { it.isDirectory }?.forEach { taskFolder ->
            println("Start handling task folder: $taskFolder")
            taskFolder.listFiles()?.filter { it.isFile && it.name.endsWith(Extension.CSV.value) }?.forEach {
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val currentDate = sdf.format(Date())
                println("Current file is ${it.path}. Current date is: $currentDate")
                val outputPath = "$outputLanguageFolder/${taskFolder.name}/${it.name}"
                // don't handle files twice
                if (!File(outputPath).exists()) {
                    val outputFile = createFile(outputPath)
                    val df = anonymizeCsvFile(it.path)
                    df.writeCSV(outputFile)
                }
            }
        }
    }

    fun anonymizeCsvFile(csvFilePath: String): DataFrame {
        val df = DataFrame.readCSV(csvFilePath)
        if (Column.FRAGMENT.key !in df.cols.map { it.name }) {
            return df
        }
        return df.addColumn(Column.FRAGMENT.key) { filePath ->
            filePath[Column.FRAGMENT.key].map<String> {
                println("Current code fragment is\n$it\n\n")
                anonymize(it).text
            }
        }
    }
}
