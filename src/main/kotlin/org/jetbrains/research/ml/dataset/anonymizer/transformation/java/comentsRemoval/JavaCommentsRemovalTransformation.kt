package org.jetbrains.research.ml.dataset.anonymizer.transformation.java.comentsRemoval

import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocToken
import org.jetbrains.research.ml.ast.transformations.Transformation
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.TransformationUtil

object JavaCommentsRemovalTransformation : Transformation {
    override val metadataKey: String
        get() = TODO("Not yet implemented")

    override fun inverseApply(psiTree: PsiElement) {
        TODO("Not yet implemented")
    }

    override fun apply(psiTree: PsiElement, toStoreMetadata: Boolean) {
        TransformationUtil.applyCommentsRemoval(psiTree, JavaCommentsRemovalVisitor(), PsiDocToken::class.java)
    }
}
