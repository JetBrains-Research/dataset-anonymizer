package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.kotlin.data.extensions

class c1 {
    val c1_f1 = 0
}

class c2 {
    val c1.c2_f1: Int
        get() = 2

    fun c2_f1(): Int {
        val c2_f1_v1 = c1()
        return c2_f1_v1.c1_f1 + c2_f1_v1.c2_f1
    }
}

fun main() {
    val main_v1 = c2()
    main_v1.c2_f1()
}
