package org.jetbrains.research.ml.dataset.anonymizer.transformation.kotlin

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.CommentsRemovalVisitor

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
