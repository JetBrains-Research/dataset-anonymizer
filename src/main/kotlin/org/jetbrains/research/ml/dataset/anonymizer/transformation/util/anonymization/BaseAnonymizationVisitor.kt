package org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.SearchScopeProvider
//import org.jetbrains.research.ml.ast.transformations.anonymization.RenameUtil.renameElementDelayed
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.refactoring.rename.RenamePsiElementProcessor.forElement
import com.intellij.usageView.UsageInfo
import org.jetbrains.kotlin.idea.refactoring.rename.*
import org.jetbrains.kotlin.psi.*
//import org.jetbrains.kotlin.idea.refactoring.rename.RenameKotlinPsiProcessor.forElement
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization.RenameUtil.renameElementDelayed

//import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization.RenameUtil.renameElementDelayed

open class BaseAnonymizationVisitor(file: PsiFile, private val anonymizer: BaseElementAnonymizer) :
    PsiRecursiveElementVisitor() {
    private val project = file.project

    override fun visitElement(element: PsiElement) {
        anonymizer.registerElement(element)
        super.visitElement(element)
    }

    // TODO: it does not work for implements something now: see Java tests: classes_and_methods/out_2.java
    open fun performAllRenames() {
        val renames = anonymizer.getAllRenames().map { renameElementDelayed(it.first, it.second) }
        WriteCommandAction.runWriteCommandAction(project) {
            renames.forEach { it() }
        }
    }
}

object RenameUtil {
    fun renameElementDelayed(definition: PsiElement, newName: String): () -> Unit {
        val processor = when(definition) {
            is KtParameter -> RenameKotlinParameterProcessor()
            is KtFunction -> RenameKotlinFunctionProcessor()
            is KtProperty -> RenameKotlinPropertyProcessor()
            is KtTypeParameter -> RenameKotlinTypeParameterProcessor()
            else -> forElement(definition)
        }
        val allRenames = mutableMapOf(definition to newName)
        processor.prepareRenaming(definition, newName, allRenames, definition.useScope)
        val delayedRenames = allRenames.map { renameSingleElementDelayed(it.key, it.value) }
        return { delayedRenames.forEach { it() } }
    }

    private fun renameSingleElementDelayed(definition: PsiElement, newName: String): () -> Unit {
        val processor = when(definition) {
            is KtParameter -> RenameKotlinParameterProcessor()
            is KtFunction -> RenameKotlinFunctionProcessor()
            is KtProperty -> RenameKotlinPropertyProcessor()
            is KtTypeParameter -> RenameKotlinTypeParameterProcessor()
            else -> forElement(definition)
        }
        val useScope = definition.useScope
        val references = processor.findReferences(definition, useScope, false)
        val usages = references.map { UsageInfo(it) }.toTypedArray()

        return { processor.renameElement(definition, newName, usages, null) }
    }
}
