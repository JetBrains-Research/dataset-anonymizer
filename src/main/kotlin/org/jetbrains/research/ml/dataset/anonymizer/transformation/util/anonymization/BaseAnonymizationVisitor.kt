package org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.search.PsiSearchHelper
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
        val renames = anonymizer.getAllRenames().map { renameElementDelayed(it.first, it.second, project) }
        WriteCommandAction.runWriteCommandAction(project) {
            renames.forEach { it() }
        }
    }
}

object RenameUtil {
    fun renameElementDelayed(definition: PsiElement, newName: String, project: Project): () -> Unit {
        val processor = forElement(definition)
        val allRenames = mutableMapOf(definition to newName)
        processor.prepareRenaming(definition, newName, allRenames)
        val delayedRenames = allRenames.map { renameSingleElementDelayed(it.key, it.value, project) }
        return { delayedRenames.forEach { it() } }
    }

    private fun renameSingleElementDelayed(definition: PsiElement, newName: String, project: Project): () -> Unit {
        val processor = forElement(definition)
        val useScope = PsiSearchHelper.getInstance(project).getUseScope(definition)
        val references = processor.findReferences(definition, useScope, false)
        val usages = references.map { UsageInfo(it) }.toTypedArray()

//        usages.map { processor.renameElement(it.element!!.parent.parent, newName, emptyList<UsageInfo>().toTypedArray(), null) }

        return { processor.renameElement(definition, newName, usages, null) }
    }
}
