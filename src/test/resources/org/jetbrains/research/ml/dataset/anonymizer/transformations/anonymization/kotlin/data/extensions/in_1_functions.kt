package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.kotlin.data.extensions

class A {
    fun a() {
        print("hello")
    }
}

fun A.b() {
    print("world")
}

class B {
    fun a() {
        print("what's")
    }
    fun A.c() {
        print("up")
    }
}
