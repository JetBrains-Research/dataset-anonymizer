package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.kotlin

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiMethod
import com.intellij.psi.impl.PsiSuperMethodImplUtil
import org.jetbrains.kotlin.idea.hierarchy.overrides.isOverrideHierarchyElement
import org.jetbrains.kotlin.idea.search.declarationsSearch.findDeepestSuperMethodsKotlinAware
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken.keywordModifier
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType
import org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.JvmTypes

object KotlinTypes : JvmTypes() {
    override fun isClass(element: PsiElement): Boolean = !isInterface(element) && (element is KtClass || element.isObject())
    override fun isStaticFunction(element: PsiElement): Boolean = element is KtFunction && element.isCompanion()
    override fun isNonStaticFunction(element: PsiElement): Boolean = element is KtFunction && !element.isCompanion()
    override fun isParameter(element: PsiElement): Boolean = element is KtParameter
    override fun isLambda(element: PsiElement): Boolean = element is KtLambdaExpression
    override fun isVariable(element: PsiElement): Boolean = element is KtProperty
    override fun isInterface(element: PsiElement): Boolean = element is KtClass && element.isInterface()
    override fun isConstructor(element: PsiElement): Boolean = element is KtSecondaryConstructor
    override fun isStatic(element: PsiMember): Boolean = element.isCompanion()
    //  Todo: find super method
    override fun getSuperMethod(element: PsiElement): PsiElement? = null

//  Todo: check that it works
    private fun PsiElement.isCompanion(): Boolean {
        val parent = this.getParentOfType<KtObjectDeclaration>(false)
        return (parent as? PsiMember)?.hasModifier("companion") ?: false
//        return parent?.hasModifier(keywordModifier("companion")) ?: false
//        return PsiTreeUtil.findFirstParent(this) { p -> p is KtObjectDeclaration } != null
    }
    private fun PsiElement.isObject(): Boolean = this is KtObjectDeclaration && !this.isCompanion()
}
