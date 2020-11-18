package org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import org.jetbrains.research.ml.ast.transformations.anonymization.RenameUtil.renameElementDelayed

open class BaseAnonymizationVisitor(file: PsiFile, private val anonymizer: BaseElementAnonymizer) :
    PsiRecursiveElementVisitor() {
    private val project = file.project

    override fun visitElement(element: PsiElement) {
        anonymizer.registerElement(element)
        super.visitElement(element)
    }

    // TODO: it does not work for implements something now: see Java tests
    open fun performAllRenames() {
        val renames = anonymizer.getAllRenames().map { renameElementDelayed(it.first, it.second) }
        WriteCommandAction.runWriteCommandAction(project) {
            renames.forEach { it() }
        }
    }
}
