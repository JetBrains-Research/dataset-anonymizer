package org.jetbrains.research.ml.dataset.anonymizer.kotlin

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.research.ml.dataset.anonymizer.Anonymizer
import org.jetbrains.research.ml.dataset.anonymizer.getTmpProjectDir
import org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.kotlin.KotlinAnonymizationTransformation
import org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval.kotlin.KotlinCommentsRemovalTransformation
import org.jetbrains.research.ml.dataset.anonymizer.util.Language

class KotlinAnonymizer(override val project: Project, tmpDataPath: String = getTmpProjectDir()) :
    Anonymizer(tmpDataPath) {
    override val language: Language
        get() = Language.KOTLIN
    override val transformations: List<(PsiElement, Boolean) -> Unit>
        get() = listOf(KotlinCommentsRemovalTransformation::apply, KotlinAnonymizationTransformation::apply)
}
