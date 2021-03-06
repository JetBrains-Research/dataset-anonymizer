package org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval.kotlin

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.research.ml.ast.transformations.Transformation

object KotlinCommentsRemovalTransformation : Transformation {
    override val metadataKey: String
        get() = TODO("Not yet implemented")

    override fun inverseApply(psiTree: PsiElement) {
        TODO("Not yet implemented")
    }

    override fun apply(psiTree: PsiElement, toStoreMetadata: Boolean) {
        val visitor = KotlinCommentsRemovalVisitor()
        visitor.applyCommentsRemoval(psiTree, KDoc::class.java)
    }
}
