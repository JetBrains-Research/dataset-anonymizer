package org.jetbrains.research.ml.dataset.anonymizer.java

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.research.ml.dataset.anonymizer.Anonymizer
import org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval.java.JavaCommentsRemovalTransformation
import org.jetbrains.research.ml.dataset.anonymizer.getTmpProjectDir
import org.jetbrains.research.ml.dataset.anonymizer.transformation.anonymization.java.JavaAnonymizationTransformation
import org.jetbrains.research.ml.dataset.anonymizer.util.Language

class JavaAnonymizer(override val project: Project, tmpDataPath: String = getTmpProjectDir()) :
    Anonymizer(tmpDataPath) {
    override val language: Language
        get() = Language.JAVA
    override val transformations: List<(PsiElement, Boolean) -> Unit>
        get() = listOf(JavaCommentsRemovalTransformation::apply, JavaAnonymizationTransformation::apply)
}
