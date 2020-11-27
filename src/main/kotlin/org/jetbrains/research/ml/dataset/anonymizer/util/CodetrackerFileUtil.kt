package org.jetbrains.research.ml.dataset.anonymizer.util

enum class Column(val key: String) {
    FRAGMENT("fragment")
}

enum class Language(val value: String) {
    PYTHON("python"),
    JAVA("java"),
    KOTLIN("kotlin");

    val extension: Extension
        get() = when (this) {
            PYTHON -> Extension.PY
            JAVA -> Extension.JAVA
            KOTLIN -> Extension.KOTLIN
            else -> error("Not implemented")
        }
}
