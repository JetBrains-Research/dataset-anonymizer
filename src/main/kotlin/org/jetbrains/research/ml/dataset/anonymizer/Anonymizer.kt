package org.jetbrains.research.ml.dataset.anonymizer

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import com.jetbrains.extensions.python.toPsi
import org.jetbrains.research.ml.dataset.anonymizer.util.FileUtil
import org.jetbrains.research.ml.dataset.anonymizer.util.FileUtil.createFile

private fun getTmpDir(): String = System.getProperty("java.io.tmpdir")

abstract class Anonymizer(private val tmpDataPath: String = getTmpDir()) {
    protected abstract val extension: FileUtil.Extension
    protected abstract val transformation: (PsiElement, Boolean) -> Unit
    protected abstract val project: Project
    private var counter: Int = 0

    fun anonymize(code: String): PsiElement {
        val file = createFile("$tmpDataPath/tmp_${counter}${extension.value}", code)
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
            ?: error("The virtual file ${file.path} was not created")
        val psi = ApplicationManager.getApplication().runReadAction<PsiElement> {
            virtualFile.toPsi(project) as PsiElement
        }
        ApplicationManager.getApplication().invokeAndWait {
            transformation(psi, false)
            ApplicationManager.getApplication().runWriteAction {
                com.intellij.openapi.util.io.FileUtil.delete(file)
            }
        }
        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
        counter++
        return psi
    }
}
