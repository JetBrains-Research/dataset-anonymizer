package org.jetbrains.research.ml.dataset.anonymizer.transformation.kotlin.anonymization

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.research.ml.ast.transformations.Transformation
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.TransformationUtil.applyAnonymization
import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.anonymization.BaseAnonymizationVisitor

class KotlinAnonymizationVisitor(file: PsiFile) : BaseAnonymizationVisitor(file, KotlinElementAnonymizer())

object KotlinAnonymizationTransformation : Transformation {
    override val metadataKey: String
        get() = TODO("Not yet implemented")

    override fun inverseApply(psiTree: PsiElement) {
        TODO("Not yet implemented")
    }

    override fun apply(psiTree: PsiElement, toStoreMetadata: Boolean) {
        val visitor = KotlinAnonymizationVisitor(psiTree.containingFile as PsiFile)
        applyAnonymization(psiTree, visitor)
    }
}
