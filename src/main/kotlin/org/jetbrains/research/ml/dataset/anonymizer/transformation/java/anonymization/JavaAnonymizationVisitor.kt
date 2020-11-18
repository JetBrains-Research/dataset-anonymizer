package org.jetbrains.research.ml.dataset.anonymizer.transformation.java.anonymization

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.jetbrains.python.psi.PyRecursiveElementVisitor
import org.jetbrains.research.ml.ast.transformations.anonymization.RenameUtil.renameElementDelayed

class JavaAnonymizationVisitor(file: PsiFile) : PsiRecursiveElementVisitor() {
    private val project = file.project
    private val anonymizer = ElementAnonymizer()

    override fun visitElement(element: PsiElement) {
        anonymizer.registerElement(element)
        super.visitElement(element)
    }

    // TODO: it does not work for implements something now
    fun performAllRenames() {
        val renames = anonymizer.getAllRenames().map { renameElementDelayed(it.first, it.second) }
        WriteCommandAction.runWriteCommandAction(project) {
            renames.forEach { it() }
        }
    }
}
