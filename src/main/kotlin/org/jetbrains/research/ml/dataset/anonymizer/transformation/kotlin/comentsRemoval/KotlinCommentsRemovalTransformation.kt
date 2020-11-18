package org.jetbrains.research.ml.dataset.anonymizer.transformation.kotlin.comentsRemoval

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.research.ml.ast.transformations.Transformation
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.TransformationUtil.applyCommentsRemoval

object KotlinCommentsRemovalTransformation : Transformation {
    override val metadataKey: String
        get() = TODO("Not yet implemented")

    override fun inverseApply(psiTree: PsiElement) {
        TODO("Not yet implemented")
    }

    override fun apply(psiTree: PsiElement, toStoreMetadata: Boolean) {
        applyCommentsRemoval(psiTree, KotlinCommentsRemovalVisitor(), KDoc::class.java)
    }
}
