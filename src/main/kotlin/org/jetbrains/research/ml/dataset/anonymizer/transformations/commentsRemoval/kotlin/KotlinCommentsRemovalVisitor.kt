package org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval.kotlin

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval.CommentsRemovalVisitor

class KotlinCommentsRemovalVisitor : CommentsRemovalVisitor() {
    override fun visitElement(element: PsiElement) {
        (element as? KDoc)?.let {
            if (element.children.toList().isNotEmpty()) {
                element.deleteChildRange(element.children.first(), element.children.last())
            }
        }
        super.visitElement(element)
    }
}
