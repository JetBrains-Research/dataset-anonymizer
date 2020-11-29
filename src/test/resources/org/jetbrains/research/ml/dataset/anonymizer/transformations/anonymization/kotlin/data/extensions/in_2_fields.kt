package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.kotlin.data.extensions

class D {
    val a = 0
}

class C {
    val c1.b: Int
        get() = 2

    fun c(): Int {
        val d = c1()
        return d.c1_f1 + d.b
    }
}

fun main() {
    val c = c2()
    c.c2_f1()
}
