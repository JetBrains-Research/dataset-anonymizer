package org.jetbrains.research.ml.dataset.anonymizer.java

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.research.ml.dataset.anonymizer.Anonymizer
import org.jetbrains.research.ml.dataset.anonymizer.transformation.java.JavaCommentsRemovalTransformation
import org.jetbrains.research.ml.dataset.anonymizer.util.Language

class JavaAnonymizer(override val project: Project, tmpDataPath: String) : Anonymizer(tmpDataPath) {
    override val language: Language
        get() = Language.JAVA
    override val transformations: List<(PsiElement, Boolean) -> Unit>
        get() = listOf(JavaCommentsRemovalTransformation::apply)
}
