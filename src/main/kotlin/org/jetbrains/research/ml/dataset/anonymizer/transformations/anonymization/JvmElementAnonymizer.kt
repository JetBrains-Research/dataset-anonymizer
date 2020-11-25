package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.impl.PsiSuperMethodImplUtil
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil.findFirstParent

class JvmElementAnonymizer(protected val types: JvmTypes)  {
    val allRenames: MutableList<Pair<PsiElement, String>> = mutableListOf()
    private val elementToNewName: MutableMap<PsiElement, String?> = mutableMapOf()
    private val parentToKindCounter: MutableMap<PsiElement?, NamedEntityKindCounter> = mutableMapOf()
    private val notToAnonymize = listOf("equals", "main", "hashCode", "toString")

    fun registerElement(element: PsiElement): String? {
        return if (types.isDefinition(element)) {
            elementToNewName.getOrPut(element) {
                findAnonName(element)?.also { newName ->
                    if (element.toRename()) {
                        allRenames.add(element to newName)
                    }
                }
            }
        } else null
    }

    private fun findAnonName(element: PsiElement): String? {
        if (!element.toAnonymize()) return null
        val definitionKind = types.getElementKind(element) ?: return null

        if (types.isFunction(element)) {
            val superMethod = PsiSuperMethodImplUtil.findDeepestSuperMethod(element as PsiMethod)
            superMethod?.let {
                registerElement(it)
                return it.scopeName
            }
        }
        val parent = getDefinitionParent(element)
        return assembleAnonName(parent, definitionKind)
    }

    private fun getDefinitionParent(definition: PsiElement): PsiElement? {
        return findFirstParent(definition, true) { p -> types.isDefinition(p) }?.also {
            registerElement(it)
        }
    }

//  ToDo: add constructor
    private fun PsiElement.toAnonymize(): Boolean
        = !(this is PsiNamedElement && this.name in notToAnonymize)

    private fun PsiElement.toRename(): Boolean
        = !types.isLambda(this)

    private fun assembleAnonName(parent: PsiElement?, kind: NamedEntityKind): String {
        val prefix = getPrefix(parent)
        val kindCount = parentToKindCounter.getOrPut(parent) { NamedEntityKindCounter() }.next(kind)
        return "$prefix${kind.prefix}$kindCount"
    }

    // todo: change back
    private val PsiElement.scopeName: String
        get() = elementToNewName[this] ?: (this as? PsiNamedElement)?.name ?: ""

    private fun getPrefix(parent: PsiElement?, toAddSeparator: Boolean = true): String {
        return parent?.scopeName?.let { if (toAddSeparator) "${it}_" else it } ?: ""
    }
}
