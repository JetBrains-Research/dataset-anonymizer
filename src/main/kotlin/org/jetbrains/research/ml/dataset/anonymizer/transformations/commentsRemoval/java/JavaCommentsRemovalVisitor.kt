package org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval.java

import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocToken
import org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval.CommentsRemovalVisitor

class JavaCommentsRemovalVisitor : CommentsRemovalVisitor() {
    override fun visitElement(element: PsiElement) {
        (element as? PsiDocToken)?.let {
            element.delete()
        }
        super.visitElement(element)
    }
}
