package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMember
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.util.PsiTreeUtil.findFirstParent

abstract class JvmTypes {
    abstract fun isClass(element: PsiElement) : Boolean
    abstract fun isStaticFunction(element: PsiElement) : Boolean
    abstract fun isNonStaticFunction(element: PsiElement) : Boolean
    abstract fun isParameter(element: PsiElement) : Boolean
    abstract fun isLambda(element: PsiElement) : Boolean
    abstract fun isVariable(element: PsiElement) : Boolean
    abstract fun isInterface(element: PsiElement) : Boolean
    abstract fun isBaseMethod(element: PsiElement) : Boolean
    abstract fun isConstructor(element: PsiElement) : Boolean
    fun isFunction(element: PsiElement) : Boolean = isStaticFunction(element) || isNonStaticFunction(element)
    fun isStaticField(element: PsiElement) : Boolean = element is PsiField && element.isStatic()
    fun isNonStaticField(element: PsiElement) : Boolean = element is PsiField && !element.isStatic()

    protected fun PsiMember.isStatic(): Boolean {
        return this.modifierList != null && this.modifierList!!.hasModifierProperty("static")
    }

    fun isDefinition(element: PsiElement): Boolean {
        return isClass(element) ||
            isFunction(element) ||
            isParameter(element) ||
            isLambda(element) ||
            isVariable(element) ||
            isInterface(element) ||
            isStaticField(element) ||
            isNonStaticField(element)
    }

    fun getElementKind(element: PsiElement): NamedEntityKind? {
        return when {
            isClass(element) -> NamedEntityKind.Class
            isInterface(element) -> NamedEntityKind.Interface
            isParameter(element) -> NamedEntityKind.Parameter
            isVariable(element) -> NamedEntityKind.Variable
            isStaticField(element) -> NamedEntityKind.StaticField
            isNonStaticField(element) -> NamedEntityKind.Field
            isStaticFunction(element) -> NamedEntityKind.StaticFunction
            isNonStaticFunction(element) -> NamedEntityKind.Function
            isLambda(element) -> NamedEntityKind.Lambda
            else -> null
        }
    }

    abstract fun getSuperMethod(element: PsiElement): PsiElement?
}

