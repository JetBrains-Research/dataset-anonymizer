package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.kotlin

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.impl.PsiSuperMethodImplUtil
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.*
import org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.JvmTypes

object KotlinTypes : JvmTypes() {
    override fun isClass(element: PsiElement): Boolean = !isInterface(element) && element is KtClass
    //    Todo: add base method
    override fun isStaticFunction(element: PsiElement): Boolean = element is KtFunction && !element.isStatic()
    override fun isNonStaticFunction(element: PsiElement): Boolean = element is KtFunction && element.isStatic()
    override fun isParameter(element: PsiElement): Boolean = element is KtParameter
    override fun isLambda(element: PsiElement): Boolean = element is KtLambdaExpression
    override fun isVariable(element: PsiElement): Boolean = element is KtProperty
    override fun isInterface(element: PsiElement): Boolean = element is KtClass && element.isInterface()
    override fun getDefinitionParent(definition: PsiElement): PsiElement? {
        if (isGlobal(definition)) return null
        return PsiTreeUtil.findFirstParent(definition) { p -> isDefinition(p) }
    }
    override fun isBaseMethod(element: PsiElement): Boolean =
        element is PsiMethod && PsiSuperMethodImplUtil.findDeepestSuperMethod(element) == null

    private fun KtFunction.isStatic(): Boolean {
        return PsiTreeUtil.findFirstParent(this) { p -> p is KtObjectDeclaration } != null
    }
}
