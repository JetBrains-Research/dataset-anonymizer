package org.jetbrains.research.ml.dataset.anonymizer.transformation.java.anonymization

import com.intellij.psi.*
import com.intellij.psi.impl.PsiSuperMethodImplUtil
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization.BaseElementAnonymizer
import kotlin.test.fail

class JavaElementAnonymizer: BaseElementAnonymizer() {
    override fun handleNotDefinition(element: PsiElement): String? {
        return when (element) {
            is PsiParameterList -> getPrefix(computeParentOfDefinition(element), false)
            is PsiMethod -> getNewNameForElement(PsiSuperMethodImplUtil.findDeepestSuperMethod(element)!!)
            is PsiCodeBlock -> getPrefix(computeParentOfDefinition(element), false)
            is PsiDeclarationStatement -> getPrefix(computeParentOfDefinition(element), false)
            else -> fail("A new name for a non-definition requested")
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
