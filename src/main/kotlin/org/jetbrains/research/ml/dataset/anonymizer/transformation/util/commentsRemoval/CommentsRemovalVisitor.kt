package org.jetbrains.research.ml.dataset.anonymizer.transformation.util.commentsRemoval

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElementVisitor

open class CommentsRemovalVisitor : PsiElementVisitor() {

    override fun visitComment(comment: PsiComment) {
        comment.delete()
        super.visitComment(comment)
    }
}
