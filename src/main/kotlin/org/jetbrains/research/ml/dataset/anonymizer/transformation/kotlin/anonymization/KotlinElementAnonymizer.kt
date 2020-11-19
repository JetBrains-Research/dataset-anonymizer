package org.jetbrains.research.ml.dataset.anonymizer.transformation.kotlin.anonymization

import com.intellij.psi.*
import com.intellij.psi.impl.PsiSuperMethodImplUtil
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.util.parentOfType
import org.jetbrains.kotlin.psi.*
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization.BaseElementAnonymizer
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization.NamedEntityKind

class KotlinElementAnonymizer : BaseElementAnonymizer() {

    private fun PsiMember.isStatic(): Boolean {
        return this.modifierList != null && this.modifierList!!.hasModifierProperty("static")
    }

    override fun getElementKind(element: PsiElement): NamedEntityKind? = when (element) {
        is KtFunction -> {
//            if (element.) {
//                NamedEntityKind.Constructor
//            } else {
//                if (element.isStatic()) {
//                    NamedEntityKind.StaticFunction
//                } else {
//                    NamedEntityKind.Function
//                }
//            }
            NamedEntityKind.Function
        }
        is PsiField -> {
            if (element.isStatic()) {
                NamedEntityKind.StaticField
            } else {
                NamedEntityKind.Field
            }
        }
        is KtClass -> {
            if (element.isInterface()) {
                NamedEntityKind.Interface
            } else {
                NamedEntityKind.Class
            }
        }
        is KtParameter -> NamedEntityKind.Parameter
        is KtProperty -> NamedEntityKind.Variable
        else -> null
    }

    override fun handleNotDefinition(element: PsiElement): String? {
        return when (element) {
            is KtFunction -> getNewNameForElement(PsiSuperMethodImplUtil.findDeepestSuperMethod(element as PsiMethod)!!)
            is PsiLambdaExpression -> assembleNewFullName(computeParentOfDefinition(element), NamedEntityKind.Lambda)
            else -> getPrefix(computeParentOfDefinition(element), false)
        }
    }

    override fun computeParentOfDefinition(definition: PsiElement): PsiElement? {
        if (!isElementGlobal(definition)) {
            val parent = definition.parent
            return (parent as? PsiDeclarationStatement)?.parent ?: parent
        }
        return null
    }

    override fun isDefinition(element: PsiElement, toCheckBaseMethod: Boolean): Boolean {
        fun KtFunction.isBaseMethod(toCheckSuperMethod: Boolean): Boolean =
            !toCheckSuperMethod

        return element is KtClass ||
            // Only consider the base method the definition
            element is KtFunction ||
//            element is PsiField ||
            element is KtParameter ||
            element is KtProperty
    }

    override fun shouldRenameDefinition(definition: PsiElement): Boolean {
        (definition as? KtFunction)?.let {
            if (definition.name in listOf("equals", "main", "hashCode", "toString")) {
                return false
            }
        }
        // TODO: check if the class or function is from a library
        return true
    }

    override fun isElementGlobal(element: PsiElement): Boolean =
        element.parentOfType<KtClass>() == null &&
            element !is KtParameter &&
            element.useScope !is LocalSearchScope &&
            element.parentOfType<KtLambdaExpression>() == null
}
