package org.jetbrains.research.ml.dataset.anonymizer.kotlin

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.research.ml.dataset.anonymizer.Anonymizer
import org.jetbrains.research.ml.dataset.anonymizer.getTmpProjectDir
import org.jetbrains.research.ml.dataset.anonymizer.transformation.kotlin.comentsRemoval.KotlinCommentsRemovalTransformation
import org.jetbrains.research.ml.dataset.anonymizer.util.Language

class KotlinAnonymizer(override val project: Project, tmpDataPath: String = getTmpProjectDir()) :
    Anonymizer(tmpDataPath) {
    override val language: Language
        get() = Language.KOTLIN
    override val transformations: List<(PsiElement, Boolean) -> Unit>
        // TODO: add anonymization
        get() = listOf(KotlinCommentsRemovalTransformation::apply)
}
