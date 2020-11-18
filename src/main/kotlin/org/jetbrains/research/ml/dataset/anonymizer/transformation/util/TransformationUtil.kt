package org.jetbrains.research.ml.dataset.anonymizer.transformation.util

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.research.ml.ast.transformations.util.PsiUtil

object TransformationUtil {
    fun applyCommentsRemoval(psiTree: PsiElement, visitor: CommentsRemovalVisitor, psiDoc: Class<out PsiElement>) {
        val comments = PsiTreeUtil.collectElementsOfType(psiTree, PsiComment::class.java)
        val docs = PsiTreeUtil.collectElementsOfType(psiTree, psiDoc)

        PsiUtil.acceptStatements(psiTree.project, comments, visitor)
        PsiUtil.acceptStatements(psiTree.project, docs, visitor)
    }
}
