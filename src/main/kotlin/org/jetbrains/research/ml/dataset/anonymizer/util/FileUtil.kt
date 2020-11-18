package org.jetbrains.research.ml.dataset.anonymizer.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import com.jetbrains.extensions.python.toPsi
import krangl.DataFrame
import krangl.map
import krangl.readCSV
import krangl.writeCSV
import org.jetbrains.research.ml.dataset.anonymizer.Anonymizer
import org.jetbrains.research.ml.dataset.anonymizer.util.CodetrackerFileUtil.Language
import java.io.File

object FileUtil {
    fun createFile(path: String, content: String = ""): File {
        val file = File(path)
        if (!file.exists()) {
            ApplicationManager.getApplication().invokeAndWait {
                ApplicationManager.getApplication().runWriteAction {
                    FileUtil.createIfDoesntExist(file)
                    file.writeText(content)
                }
            }
        }
        return file
    }

    fun handleLanguageFolder(outputRootPath: String, language: Language, anonymizer: Anonymizer) {
        val languageFolder = File("$outputRootPath/${language.value}")
        languageFolder.listFiles()?.filter { it.isDirectory }?.forEach { taskFolder ->
            taskFolder.listFiles()?.filter { it.isFile && it.name.endsWith(Extension.CSV.value) }?.forEach {
                val df = handleCsvFile(it.path, anonymizer)
                val outputPath = "$outputRootPath/${languageFolder.name}/${taskFolder.name}/${it.name}"
                val outputFile = createFile(outputPath)
                df.writeCSV(outputFile)
            }
        }
    }

    fun handleCsvFile(csvFilePath: String, anonymizer: Anonymizer): DataFrame {
        val df = DataFrame.readCSV(csvFilePath)
        if (CodetrackerFileUtil.Column.FRAGMENT.key !in df.cols.map { it.name }) {
            return df
        }
        return df.addColumn(CodetrackerFileUtil.Column.FRAGMENT.key) { filePath ->
            filePath[CodetrackerFileUtil.Column.FRAGMENT.key].map<String> {
                anonymizer.anonymize(it).text
            }
        }
    }

    fun getPsiElement(file: File, project: Project): PsiElement {
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
            ?: error("The virtual file ${file.path} was not created")
        return ApplicationManager.getApplication().runReadAction<PsiElement> {
            virtualFile.toPsi(project) as PsiElement
        }
    }

    enum class Extension(val value: String) {
        CSV(".csv"),
        PY(".py"),
        JAVA(".java"),
        KOTLIN(".kt")
    }
}
