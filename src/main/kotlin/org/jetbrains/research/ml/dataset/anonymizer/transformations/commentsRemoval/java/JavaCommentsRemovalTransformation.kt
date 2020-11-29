package org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval.java

import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocToken
import org.jetbrains.research.ml.ast.transformations.Transformation

object JavaCommentsRemovalTransformation : Transformation {
    override val metadataKey: String
        get() = TODO("Not yet implemented")

    override fun inverseApply(psiTree: PsiElement) {
        TODO("Not yet implemented")
    }

    override fun apply(psiTree: PsiElement, toStoreMetadata: Boolean) {
        val visitor = JavaCommentsRemovalVisitor()
        visitor.applyCommentsRemoval(psiTree, PsiDocToken::class.java)
    }
}
