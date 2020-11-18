package org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization

import com.intellij.psi.*
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.parentOfType

// TODO: it does not work with lambda
abstract class BaseElementAnonymizer {
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

    protected fun getNewNameForElement(element: PsiElement): String? =
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
            return handleNotDefinition(element)
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

    protected open fun shouldRenameDefinition(definition: PsiElement): Boolean = true

    protected open fun getPrefix(parent: PsiElement?, toAddSeparator: Boolean = true): String {
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
        val prefix = getPrefix(parent)
        val kindCount = parentToKindCounter.getOrPut(parent) { NamedEntityKindCounter() }.next(kind)
        return "$prefix${kind.prefix}$kindCount"
    }

    protected fun isElementGlobal(element: PsiElement): Boolean =
        element.parentOfType<PsiClass>() == null &&
            element !is PsiParameter &&
            element.useScope !is LocalSearchScope &&
            element.parentOfType<PsiLambdaExpression>() == null

    protected open fun computeParentOfDefinition(definition: PsiElement): PsiElement? =
        if (!isElementGlobal(definition)) definition.parent else null

    // Todo: rename argument
    abstract fun isDefinition(element: PsiElement, toCheckBaseMethod: Boolean = true): Boolean

    abstract fun handleNotDefinition(element: PsiElement): String?
}
