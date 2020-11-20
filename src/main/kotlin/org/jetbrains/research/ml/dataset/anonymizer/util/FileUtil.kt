package org.jetbrains.research.ml.dataset.anonymizer.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.io.FileUtil
import java.io.File

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

enum class Extension(val value: String) {
    CSV(".csv"),
    PY(".py"),
    JAVA(".java"),
    KOTLIN(".kt");
}
