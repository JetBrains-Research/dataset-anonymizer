package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.refactoring.rename.RenamePsiElementProcessor.forElement
import com.intellij.util.IncorrectOperationException
import org.jetbrains.kotlin.resolve.lazy.NoDescriptorForDeclarationException
import java.util.logging.Logger

open class JvmAnonymizationVisitor(file: PsiFile, val anonymizer: JvmElementAnonymizer) :
    PsiRecursiveElementVisitor() {
    private val project = file.project
    protected val log = Logger.getLogger(javaClass.name)

    override fun visitElement(element: PsiElement) {
        anonymizer.registerElement(element)
        super.visitElement(element)
    }

    fun applyAnonymization(psiElement: PsiElement) {
        psiElement.accept(this)
        performAllRenames()
    }

    // TODO: it does not work for implements something now: see Java tests: classes_and_methods/out_2.java
    private fun performAllRenames() {
        val renames = anonymizer.allRenames.map { renameElementDelayed(it.first, it.second) }
        WriteCommandAction.runWriteCommandAction(project) {
            renames.forEach { it() }
        }
    }

    private fun renameElementDelayed(definition: PsiElement, newName: String): () -> Unit {
        val processor = forElement(definition)
        try {
            processor.prepareRenaming(definition, newName, mutableMapOf(definition to newName))
        } catch (e: NoDescriptorForDeclarationException) {
            log.info("${e.message} for element $definition with new name $newName (maybe the code is incorrect)")
            return { }
        }
        val references = processor.findReferences(definition, definition.useScope, false)
        val usages = references.map { processor.createUsageInfo(definition, it, it.element) }.toTypedArray()
        return {
            try {
                processor.renameElement(definition, newName, usages, null)
            } catch (e: IncorrectOperationException) {
                log.info(
                    "IncorrectOperationException: Cannot perform the renaming for " +
                        "element $definition with new name $newName"
                )
            } catch (e: NullPointerException) {
                log.info(
                    "NullPointerException: Cannot perform the renaming " +
                        "for element $definition with new name $newName"
                )
            }
        }
    }
}
