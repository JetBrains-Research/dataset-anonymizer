package org.jetbrains.research.ml.dataset.anonymizer.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.io.FileUtil
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

    enum class Extension(val value: String) {
        CSV(".csv"),
        PY(".py")
    }
}
