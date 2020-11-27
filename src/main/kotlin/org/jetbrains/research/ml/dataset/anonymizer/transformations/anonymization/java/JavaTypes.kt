package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.java

import com.intellij.psi.*
import org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.JvmTypes

object JavaTypes : JvmTypes() {
    override fun isClass(element: PsiElement?): Boolean = !isInterface(element) && element is PsiClass
    override fun isFunction(element: PsiElement?): Boolean = element is PsiMethod
    override fun isStaticFunction(element: PsiElement?): Boolean = element is PsiMethod && element.isStatic()
    override fun isNonStaticFunction(element: PsiElement?): Boolean = element is PsiMethod && !element.isStatic()
    override fun isParameter(element: PsiElement?): Boolean = element is PsiParameter
    override fun isLambda(element: PsiElement?): Boolean = element is PsiLambdaExpression
    override fun isVariable(element: PsiElement?): Boolean =
        element is PsiLocalVariable && element.parent is PsiDeclarationStatement
    override fun isInterface(element: PsiElement?): Boolean = element is PsiClass && element.isInterface
    override fun isConstructor(element: PsiElement?): Boolean = element is PsiMethod && element.isConstructor
    override fun isStaticField(element: PsiElement?): Boolean = element is PsiField && element.isStatic()
    override fun isNonStaticField(element: PsiElement?): Boolean = element is PsiField && !element.isStatic()
    override fun getSuperMethod(element: PsiElement): PsiElement? = getSuperMethod(element as PsiMethod)

    private fun PsiMember.isStatic(): Boolean = hasModifier("static")
    private fun PsiMember.hasModifier(modifier: String): Boolean {
        return this.modifierList != null && this.modifierList!!.hasModifierProperty(modifier)
    }
}
