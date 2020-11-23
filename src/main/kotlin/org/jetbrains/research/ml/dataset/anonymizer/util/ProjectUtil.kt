package org.jetbrains.research.ml.dataset.anonymizer.util

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.ml.dataset.anonymizer.getTmpProjectDir
import java.nio.file.Path

fun createNewProject(): Project? {
    return ProjectUtil.openOrImport(Path.of(getTmpProjectDir()))
//    return ProjectUtil.openOrImport(Path.of(getTmpDir()))
}
