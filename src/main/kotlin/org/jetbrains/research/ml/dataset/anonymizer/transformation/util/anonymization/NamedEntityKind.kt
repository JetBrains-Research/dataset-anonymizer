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
    companion object {
        private fun PsiMember.isStatic(): Boolean {
            return this.modifierList != null && this.modifierList!!.hasModifierProperty("static")
        }

        fun getElementKind(element: PsiElement): NamedEntityKind? = when (element) {
            is PsiMethod -> {
                if (element.isConstructor) {
                    Constructor
                } else {
                    if (element.isStatic()) {
                        StaticFunction
                    } else {
                        Function
                    }
                }
            }
            is PsiField -> {
                if (element.isStatic()) {
                    StaticField
                } else {
                    Field
                }
            }
            is PsiClass -> {
                if (element.isInterface) {
                    Interface
                } else {
                    Class
                }
            }
            is PsiParameter -> Parameter
            is PsiLocalVariable -> Variable
            else -> null
        }
    }
}

class NamedEntityKindCounter {
    private val counter: MutableMap<NamedEntityKind, Int> = mutableMapOf()

    fun next(kind: NamedEntityKind): Int {
        val nextValue = counter.getOrDefault(kind, 0) + 1
        counter[kind] = nextValue
        return nextValue
    }
}
