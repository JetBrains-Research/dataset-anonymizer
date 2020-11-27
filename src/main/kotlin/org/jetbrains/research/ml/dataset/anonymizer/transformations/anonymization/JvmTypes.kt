package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.impl.PsiSuperMethodImplUtil

abstract class JvmTypes {
    abstract fun isClass(element: PsiElement?): Boolean
    abstract fun isFunction(element: PsiElement?): Boolean
    abstract fun isStaticFunction(element: PsiElement?): Boolean
    abstract fun isNonStaticFunction(element: PsiElement?): Boolean
    abstract fun isParameter(element: PsiElement?): Boolean
    abstract fun isLambda(element: PsiElement?): Boolean
    abstract fun isVariable(element: PsiElement?): Boolean
    abstract fun isInterface(element: PsiElement?): Boolean
    abstract fun isConstructor(element: PsiElement?): Boolean
    abstract fun isField(element: PsiElement?): Boolean
    abstract fun isStaticField(element: PsiElement?): Boolean
    abstract fun isNonStaticField(element: PsiElement?): Boolean

    open fun isDefinition(element: PsiElement): Boolean {
        return isClass(element) ||
            isFunction(element) ||
            isParameter(element) ||
            isLambda(element) ||
            isVariable(element) ||
            isInterface(element) ||
            isField(element)
    }

    open fun getElementKind(element: PsiElement): NamedEntityKind? {
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
    protected fun getSuperMethod(method: PsiMethod): PsiElement? {
        return PsiSuperMethodImplUtil.findDeepestSuperMethod(method)
    }

    /**
     * We want to store some anonymized names, but cannot rename elements (for example, lambda functions
     * or others that don't have a name)
     */
    open fun toRename(element: PsiElement): Boolean = element is PsiNamedElement
}
