package org.jetbrains.research.ml.dataset.anonymizer.transformation.java.anonymization

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyFile
import org.jetbrains.research.ml.ast.transformations.Transformation
import org.jetbrains.research.ml.ast.transformations.anonymization.AnonymizationVisitor

object JavaAnonymizationTransformation : Transformation {
    override val metadataKey: String
        get() = TODO("Not yet implemented")

    override fun inverseApply(psiTree: PsiElement) {
        TODO("Not yet implemented")
    }

    override fun apply(psiTree: PsiElement, toStoreMetadata: Boolean) {
        val visitor = JavaAnonymizationVisitor(psiTree.containingFile as PsiFile)
        psiTree.accept(visitor)
        visitor.performAllRenames()
    }

}
