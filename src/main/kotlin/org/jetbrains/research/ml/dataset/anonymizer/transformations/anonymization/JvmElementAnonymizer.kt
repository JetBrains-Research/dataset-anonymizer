package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil.findFirstParent

open class JvmElementAnonymizer(protected val types: JvmTypes)  {
    val allRenames: MutableList<Pair<PsiElement, String>> = mutableListOf()
    private val elementToNewName: MutableMap<PsiElement, String?> = mutableMapOf()
    private val parentToKindCounter: MutableMap<PsiElement?, NamedEntityKindCounter> = mutableMapOf()
    protected val notToAnonymize = listOf("equals", "main", "hashCode", "toString")

    fun registerElement(element: PsiElement) {
        if (types.isDefinition(element)) {
            elementToNewName.getOrPut(element) {
                findAnonName(element)?.also { newName ->
                    if (element.toRename()) {
                        allRenames.add(element to newName)
                    }
                }
            }
        }
    }

    private fun findAnonName(element: PsiElement): String? {
        if (!toAnonymize(element)) return null
        val parent = getDefinitionParent(element)
//      Take already registered name for constructors and overridden methods
        when {
            types.isConstructor(element) -> parent
            types.isFunction(element) -> types.getSuperMethod(element)
            else -> null
        }?.let { return it.registeredScopeName }

        val definitionKind = types.getElementKind(element) ?: return null
        return assembleAnonName(parent, definitionKind)
    }

    private fun getDefinitionParent(definition: PsiElement): PsiElement? {
        return findFirstParent(definition, true) { p -> types.isDefinition(p) }?.also {
//          Insure the parent is registered already to take its name as a prefix
            registerElement(it)
        }
    }

    /**
     * We don't want to anonymize some names (for example, functions like 'equals' or 'main')
     */
    protected open fun toAnonymize(element: PsiElement): Boolean
        = !(types.isFunction(element) && element is PsiNamedElement && element.name in notToAnonymize)

    /**
     * We want to store some anonymized names, but cannot rename elements (for example, lambda functions)
     */
    private fun PsiElement.toRename(): Boolean
        = !types.isLambda(this)

    private fun assembleAnonName(parent: PsiElement?, kind: NamedEntityKind): String {
        val prefix = getPrefix(parent)
        val kindCount = parentToKindCounter.getOrPut(parent) { NamedEntityKindCounter() }.next(kind)
        return "$prefix${kind.prefix}$kindCount"
    }

    private val PsiElement.registeredScopeName: String?
        get() {
            registerElement(this)
            return this.scopeName
        }

    private val PsiElement.scopeName: String
        get() = elementToNewName[this] ?: (this as? PsiNamedElement)?.name ?: ""

    private fun getPrefix(parent: PsiElement?, toAddSeparator: Boolean = true): String {
        return parent?.scopeName?.let { if (toAddSeparator) "${it}_" else it } ?: ""
    }
}
