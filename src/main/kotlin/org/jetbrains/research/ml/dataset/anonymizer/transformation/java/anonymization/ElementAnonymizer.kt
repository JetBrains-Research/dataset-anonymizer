package org.jetbrains.research.ml.dataset.anonymizer.transformation.java.anonymization

import com.intellij.psi.*
import com.intellij.psi.impl.PsiSuperMethodImplUtil
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.parentOfType
import kotlin.test.fail

// TODO: it does not work with lambda
class ElementAnonymizer {
    private val allRenames: MutableList<Pair<PsiElement, String>> = mutableListOf()
    private val elementToNewName: MutableMap<PsiElement, String?> = mutableMapOf()
    private val parentToKindCounter: MutableMap<PsiElement?, NamedEntityKindCounter> = mutableMapOf()

    fun registerElement(element: PsiElement) {
        if (isDefinition(element)) {
            elementToNewName.getOrPut(element) {
                computeNewNameForElement(element)?.also { newName ->
                    allRenames.add(element to newName)
                }
            }
        }
    }

    fun getAllRenames(): List<Pair<PsiElement, String>> = allRenames

    private fun getNewNameForElement(element: PsiElement): String? =
        elementToNewName.getOrPut(element) {
            computeNewNameForElement(element)?.also { newName ->
                if (isDefinition(element, false)) {
                    allRenames.add(element to newName)
                }
            }
        }

    private fun getScopeName(element: PsiElement): String =
        getNewNameForElement(element) ?: (element as PsiNamedElement).name!!

    private fun computeNewNameForElement(element: PsiElement): String? {
        if (!isDefinition(element)) {
            return when (element) {
                is PsiParameterList -> getPrefix(computeParentOfDefinition(element), false)
                is PsiMethod -> getNewNameForElement(PsiSuperMethodImplUtil.findDeepestSuperMethod(element)!!)
                is PsiCodeBlock -> getPrefix(computeParentOfDefinition(element), false)
                is PsiDeclarationStatement -> getPrefix(computeParentOfDefinition(element), false)
                else -> fail("A new name for a non-definition requested")
            }
        }
        if (!shouldRenameDefinition(element)) return null
        val parent = computeParentOfDefinition(element)
        val definitionKind = NamedEntityKind.getElementKind(element) ?: return null
        return assembleNewFullName(parent, definitionKind).also { newName ->
            for (reference in ReferencesSearch.search(element, element.useScope)) {
                elementToNewName[reference.element] = newName
            }
        }
    }

    private fun shouldRenameDefinition(definition: PsiElement): Boolean {
        (definition as? PsiMethod)?.let {
            if (definition.name in listOf("equals", "main", "hashCode", "toString")) {
                return false
            }
        }
        // TODO: check if the class or function is from a library
        return true
    }

    private fun getPrefix(parent: PsiElement?, toAddSeparator: Boolean = true): String {
        return parent?.let {
            val scopeName = getScopeName(it)
            return if (toAddSeparator) {
                "${scopeName}_"
            } else {
                scopeName
            }
        } ?: ""
    }

    private fun assembleNewFullName(parent: PsiElement?, kind: NamedEntityKind): String {
        val currentParent = (parent as? PsiDeclarationStatement)?.parent ?: parent
        val prefix = getPrefix(currentParent)
        val kindCount = parentToKindCounter.getOrPut(currentParent) { NamedEntityKindCounter() }.next(kind)
        return "$prefix${kind.prefix}$kindCount"
    }

    private fun isElementGlobal(element: PsiElement): Boolean =
        element.parentOfType<PsiClass>() == null &&
            element !is PsiParameter &&
            element.useScope !is LocalSearchScope &&
            element.parentOfType<PsiLambdaExpression>() == null

    private fun computeParentOfDefinition(definition: PsiElement): PsiElement? =
        if (!isElementGlobal(definition)) definition.parent else null

    private fun isDefinition(element: PsiElement, toCheckSuperMethod: Boolean = true): Boolean {
        return element is PsiClass ||
            // Only consider the base method the definition
            element is PsiMethod && (if (toCheckSuperMethod) PsiSuperMethodImplUtil.findDeepestSuperMethod(element) == null else true) ||
            element is PsiField ||
            element is PsiParameter ||
            element is PsiLocalVariable && element.parent is PsiDeclarationStatement
    }
}
