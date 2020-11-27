package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.kotlin

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.research.ml.ast.transformations.Transformation
import org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.JvmAnonymizationVisitor
import org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.JvmElementAnonymizer

class KotlinAnonymizationVisitor(file: PsiFile) : JvmAnonymizationVisitor(file, JvmElementAnonymizer(KotlinTypes))

object KotlinAnonymizationTransformation : Transformation {
    override val metadataKey: String
        get() = TODO("Not yet implemented")

    override fun inverseApply(psiTree: PsiElement) {
        TODO("Not yet implemented")
    }

    override fun apply(psiTree: PsiElement, toStoreMetadata: Boolean) {
        val visitor = KotlinAnonymizationVisitor(psiTree.containingFile as PsiFile)
        visitor.applyAnonymization(psiTree)
    }
}
