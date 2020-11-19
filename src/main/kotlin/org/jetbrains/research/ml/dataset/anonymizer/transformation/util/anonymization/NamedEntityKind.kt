package org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization

import com.intellij.psi.*

enum class NamedEntityKind(val prefix: String) {
    Function("f"),
    StaticFunction("sf"),
    Variable("v"),
    Class("c"),
    Constructor("init"),
    Field("f"),
    StaticField("sf"),
    Parameter("p"),
    Interface("i"),
    Lambda("l");
}

class NamedEntityKindCounter {
    private val counter: MutableMap<NamedEntityKind, Int> = mutableMapOf()

    fun next(kind: NamedEntityKind): Int {
        val nextValue = counter.getOrDefault(kind, 0) + 1
        counter[kind] = nextValue
        return nextValue
    }
}
