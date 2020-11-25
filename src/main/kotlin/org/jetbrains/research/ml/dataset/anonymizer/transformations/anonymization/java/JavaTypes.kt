package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.java

import com.intellij.psi.*
import com.intellij.psi.impl.PsiSuperMethodImplUtil
import org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.JvmTypes

object JavaTypes : JvmTypes() {
    override fun isClass(element: PsiElement): Boolean = !isInterface(element) && element is PsiClass
    override fun isStaticFunction(element: PsiElement): Boolean = element is PsiMethod && element.isStatic()
    override fun isNonStaticFunction(element: PsiElement): Boolean = element is PsiMethod && !element.isStatic()
    override fun isParameter(element: PsiElement): Boolean = element is PsiParameter
    override fun isLambda(element: PsiElement): Boolean = element is PsiLambdaExpression
    override fun isVariable(element: PsiElement): Boolean = element is PsiLocalVariable && element.parent is PsiDeclarationStatement
    override fun isInterface(element: PsiElement): Boolean = element is PsiClass && element.isInterface
    override fun getDefinitionParent(definition: PsiElement): PsiElement? {
        if (isGlobal(definition)) return null
        val parent = definition.parent
        return (parent as? PsiDeclarationStatement)?.parent ?: parent
    }

    override fun isBaseMethod(element: PsiElement): Boolean =
        element is PsiMethod && PsiSuperMethodImplUtil.findDeepestSuperMethod(element) == null
}