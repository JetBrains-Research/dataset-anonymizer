package org.jetbrains.research.ml.dataset.anonymizer.python

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.research.ml.ast.transformations.anonymization.AnonymizationTransformation
import org.jetbrains.research.ml.dataset.anonymizer.Anonymizer
import org.jetbrains.research.ml.dataset.anonymizer.util.FileUtil

class PythonAnonymizer(override val project: Project, tmpDataPath: String) : Anonymizer(tmpDataPath) {
    override val extension: FileUtil.Extension
        get() = FileUtil.Extension.PY
    override val transformation: (PsiElement, Boolean) -> Unit
        get() = AnonymizationTransformation::apply
}
