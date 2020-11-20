package org.jetbrains.research.ml.dataset.anonymizer

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import com.jetbrains.extensions.python.toPsi
import krangl.DataFrame
import krangl.map
import krangl.readCSV
import krangl.writeCSV
import org.jetbrains.research.ml.dataset.anonymizer.util.Column
import org.jetbrains.research.ml.dataset.anonymizer.util.Extension
import org.jetbrains.research.ml.dataset.anonymizer.util.Language
import org.jetbrains.research.ml.dataset.anonymizer.util.createFile
import java.io.File

private fun getTmpDir(): String = System.getProperty("java.io.tmpdir")

abstract class Anonymizer(private val tmpDataPath: String = getTmpDir()) {
    protected abstract val language: Language
    protected abstract val transformations: List<(PsiElement, Boolean) -> Unit>
    protected abstract val project: Project
    private var counter: Int = 0

    fun anonymize(code: String): PsiElement {
        val file = createFile("$tmpDataPath/tmp_${counter}${language.extension.value}", code)
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
            ?: error("The virtual file ${file.path} was not created")
        val psi = ApplicationManager.getApplication().runReadAction<PsiElement> {
            virtualFile.toPsi(project) as PsiElement
        }
        ApplicationManager.getApplication().invokeAndWait {
            transformations.forEach { it(psi, false) }
            ApplicationManager.getApplication().runWriteAction {
                com.intellij.openapi.util.io.FileUtil.delete(file)
            }
        }
        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
        counter++
        return psi
    }

    /**
     * The root folder need have the following structure:
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
            taskFolder.listFiles()?.filter { it.isFile && it.name.endsWith(Extension.CSV.value) }?.forEach {
                val df = anonymizeCsvFile(it.path)
                val outputPath = "$outputLanguageFolder/${taskFolder.name}/${it.name}"
                val outputFile = createFile(outputPath)
                df.writeCSV(outputFile)
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
                anonymize(it).text
            }
        }
    }
}
