package org.jetbrains.research.ml.dataset.anonymizer.transformation.java

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.javadoc.PsiDocToken
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.research.ml.ast.transformations.Transformation
import org.jetbrains.research.ml.ast.transformations.util.PsiUtil
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.CommentsRemovalVisitor

object JavaCommentsRemovalTransformation : Transformation {
    override val metadataKey: String
        get() = TODO("Not yet implemented")

    override fun inverseApply(psiTree: PsiElement) {
        TODO("Not yet implemented")
    }

    override fun apply(psiTree: PsiElement, toStoreMetadata: Boolean) {
        val comments = PsiTreeUtil.collectElementsOfType(psiTree, PsiComment::class.java)
        val docs = PsiTreeUtil.collectElementsOfType(psiTree, PsiDocToken::class.java).reversed()
        val visitor = CommentsRemovalVisitor()

        PsiUtil.acceptStatements(psiTree.project, comments, visitor)
        PsiUtil.acceptStatements(psiTree.project, docs, visitor)
    }
}
