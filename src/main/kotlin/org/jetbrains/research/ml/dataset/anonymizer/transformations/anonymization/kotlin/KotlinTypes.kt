package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.kotlin

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil.findFirstParent
import org.jetbrains.kotlin.idea.refactoring.checkSuperMethods
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.JvmTypes

object KotlinTypes : JvmTypes() {
    override fun isClass(element: PsiElement?): Boolean =
        (element is KtClass || element.isObject()) && !isInterface(element)
    override fun isFunction(element: PsiElement?): Boolean = element is KtFunction
    override fun isStaticFunction(element: PsiElement?): Boolean = isFunction(element) && element.isStatic()
    override fun isNonStaticFunction(element: PsiElement?): Boolean = isFunction(element) && !element.isStatic()
    override fun isParameter(element: PsiElement?): Boolean = element is KtParameter
    override fun isLambda(element: PsiElement?): Boolean = element is KtLambdaExpression
    override fun isVariable(element: PsiElement?): Boolean = element is KtProperty && !isField(element)
    override fun isInterface(element: PsiElement?): Boolean = element is KtClass && element.isInterface()
    override fun isConstructor(element: PsiElement?): Boolean = element is KtSecondaryConstructor
    override fun isStaticField(element: PsiElement?): Boolean = isField(element) && element.isStatic()
    override fun isNonStaticField(element: PsiElement?): Boolean = isField(element) && !element.isStatic()

    /**
     * In kotlin fields are also KtProperty (as variables), so we need to check the parent of it to be sure.
     * However, we still want to distinguish fields and variables to be more accurate in comparing anonymized
     * pieces of code.
     */
    override fun isField(element: PsiElement?): Boolean =
        element is KtProperty && isClass(findFirstParent(element) { isFunction(it) || isClass(it) })

    override fun getSuperMethod(element: PsiElement): PsiElement? {
        return if (element is KtFunction && element.hasModifier(KtTokens.OVERRIDE_KEYWORD)) {
            checkSuperMethods(element, emptyList(), "").lastOrNull()
        } else null
    }

    private fun PsiElement?.isObject(): Boolean = this is KtObjectDeclaration && !this.isCompanion()

    /**
     * A PsiElement is static if it's inside a class body of an object
     */
    private fun PsiElement?.isStatic(): Boolean =
        this?.parent is KtClassBody && this.parent?.parent is KtObjectDeclaration
}
