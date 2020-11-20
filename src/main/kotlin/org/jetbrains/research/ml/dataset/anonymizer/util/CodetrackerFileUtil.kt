package org.jetbrains.research.ml.dataset.anonymizer.util

enum class Column(val key: String) {
    FRAGMENT("fragment")
}

enum class Language(val value: String) {
    PYTHON("python"),
    JAVA("java");

    val extension: Extension
        get() = when (this) {
            PYTHON -> Extension.PY
            JAVA -> Extension.JAVA
            else -> error("Not implemented")
        }
}
