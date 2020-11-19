package org.jetbrains.research.ml.dataset.anonymizer.transformation.java.anonymization

import com.intellij.psi.*
import com.intellij.psi.impl.PsiSuperMethodImplUtil
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization.BaseElementAnonymizer
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization.NamedEntityKind

class JavaElementAnonymizer : BaseElementAnonymizer() {

    private fun PsiMember.isStatic(): Boolean {
        return this.modifierList != null && this.modifierList!!.hasModifierProperty("static")
    }

    override fun getElementKind(element: PsiElement): NamedEntityKind? = when (element) {
        is PsiMethod -> {
            if (element.isConstructor) {
                NamedEntityKind.Constructor
            } else {
                if (element.isStatic()) {
                    NamedEntityKind.StaticFunction
                } else {
                    NamedEntityKind.Function
                }
            }
        }
        is PsiField -> {
            if (element.isStatic()) {
                NamedEntityKind.StaticField
            } else {
                NamedEntityKind.Field
            }
        }
        is PsiClass -> {
            if (element.isInterface) {
                NamedEntityKind.Interface
            } else {
                NamedEntityKind.Class
            }
        }
        is PsiParameter -> NamedEntityKind.Parameter
        is PsiLocalVariable -> NamedEntityKind.Variable
        else -> null
    }

    override fun handleNotDefinition(element: PsiElement): String? {
        return when (element) {
            is PsiMethod -> getNewNameForElement(PsiSuperMethodImplUtil.findDeepestSuperMethod(element)!!)
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
        fun PsiMethod.isBaseMethod(toCheckSuperMethod: Boolean): Boolean =
            !toCheckSuperMethod || PsiSuperMethodImplUtil.findDeepestSuperMethod(this) == null

        return element is PsiClass ||
            // Only consider the base method the definition
            element is PsiMethod && element.isBaseMethod(toCheckBaseMethod) ||
            element is PsiField ||
            element is PsiParameter ||
            element is PsiLocalVariable && element.parent is PsiDeclarationStatement
    }

    override fun shouldRenameDefinition(definition: PsiElement): Boolean {
        (definition as? PsiMethod)?.let {
            if (definition.name in listOf("equals", "main", "hashCode", "toString")) {
                return false
            }
        }
        // TODO: check if the class or function is from a library
        return true
    }
}
