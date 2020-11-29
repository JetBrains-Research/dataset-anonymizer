package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.java

import com.intellij.psi.*
import org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.JvmTypes

object JavaTypes : JvmTypes() {
    override fun isClass(element: PsiElement?): Boolean = element is PsiClass && !isInterface(element)
    override fun isFunction(element: PsiElement?): Boolean = element is PsiMethod
    override fun isStaticFunction(element: PsiElement?): Boolean = isFunction(element) && element.isStatic()
    override fun isNonStaticFunction(element: PsiElement?): Boolean = isFunction(element) && !element.isStatic()
    override fun isParameter(element: PsiElement?): Boolean = element is PsiParameter
    override fun isLambda(element: PsiElement?): Boolean = element is PsiLambdaExpression
    override fun isVariable(element: PsiElement?): Boolean =
        element is PsiLocalVariable && element.parent is PsiDeclarationStatement
    override fun isInterface(element: PsiElement?): Boolean = element is PsiClass && element.isInterface
    override fun isConstructor(element: PsiElement?): Boolean = element is PsiMethod && element.isConstructor
    override fun isField(element: PsiElement?): Boolean = element is PsiField
    override fun isStaticField(element: PsiElement?): Boolean = isField(element) && element.isStatic()
    override fun isNonStaticField(element: PsiElement?): Boolean = isField(element) && !element.isStatic()
    override fun getSuperMethod(element: PsiElement): PsiElement? = getSuperMethod(element as PsiMethod)
    private fun PsiElement?.isStatic(): Boolean = (this as? PsiMember).hasModifier("static")
    private fun PsiMember?.hasModifier(modifier: String): Boolean {
        return this?.modifierList != null && this.modifierList!!.hasModifierProperty(modifier)
    }
}
