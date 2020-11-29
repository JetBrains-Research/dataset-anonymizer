package org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.research.ml.ast.transformations.util.PsiUtil

open class CommentsRemovalVisitor : PsiElementVisitor() {

    override fun visitComment(comment: PsiComment) {
        comment.delete()
        super.visitComment(comment)
    }

    fun applyCommentsRemoval(psiTree: PsiElement, psiDoc: Class<out PsiElement>) {
        val comments = PsiTreeUtil.collectElementsOfType(psiTree, PsiComment::class.java)
        val docs = PsiTreeUtil.collectElementsOfType(psiTree, psiDoc)
        PsiUtil.acceptStatements(psiTree.project, comments + docs, this)
    }
}
