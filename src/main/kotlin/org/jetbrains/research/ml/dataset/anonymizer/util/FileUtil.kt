package org.jetbrains.research.ml.dataset.anonymizer.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.io.FileUtil
import java.io.File

object FileUtil {
    fun createFile(path: String, content: String = ""): File {
        val file = File(path)
        if (!file.exists()) {
            ApplicationManager.getApplication().runWriteAction {
                FileUtil.createIfDoesntExist(file)
                file.writeText(content)
            }
        }
     //   AnonymizationTransformation
        return file
    }

//    fun handleLanguageFolder(languageFolderPath: String, outputRootPath: String) {
//        val languageFolder = File(languageFolderPath)
//        languageFolder.listFiles()?.filter { it.isDirectory }?.forEach { taskFolder ->
//            taskFolder.listFiles()?.filter { it.isFile && it.name.endsWith(Extension.Csv.value) }?.forEach {
//                val df = handleCsvFile(it.path)
//                val outputPath = "$outputRootPath/${languageFolder.name}/${taskFolder.name}/${it.name}"
//                val outputFile = createFile(outputPath)
//                df.writeCSV(outputFile)
//            }
//        }
//    }
}
