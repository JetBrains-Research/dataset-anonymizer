package org.jetbrains.research.ml.dataset.anonymizer.transformation.java

import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocToken
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.CommentsRemovalVisitor

class JavaCommentsRemovalVisitor : CommentsRemovalVisitor() {
    override fun visitElement(element: PsiElement) {
        (element as? PsiDocToken)?.let {
            element.delete()
        }
        super.visitElement(element)
    }
}
