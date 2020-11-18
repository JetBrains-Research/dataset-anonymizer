package org.jetbrains.research.ml.dataset.anonymizer.transformation.util

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.javadoc.PsiDocToken

open class CommentsRemovalVisitor : PsiElementVisitor() {

    override fun visitComment(comment: PsiComment) {
        comment.delete()
        super.visitComment(comment)
    }

    override fun visitElement(element: PsiElement) {
        (element as? PsiDocToken)?.let{
            element.delete()
        }
        super.visitElement(element)
    }
}
