package org.jetbrains.research.ml.dataset.anonymizer.util

object CodetrackerFileUtil {
    enum class Column(val key: String) {
        FRAGMENT("fragment")
    }

    enum class Language(val value: String) {
        PYTHON("python")
    }
}
