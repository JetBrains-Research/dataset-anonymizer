package org.jetbrains.research.ml.dataset.anonymizer.transformations.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.testFramework.PsiTestUtil
import org.jetbrains.research.ml.dataset.anonymizer.util.*
import org.junit.Before
import org.junit.Ignore
import org.junit.runners.Parameterized
import java.io.File
import kotlin.reflect.KFunction

@Ignore
open class TransformationsTest(testDataRoot: String) : ParametrizedBaseTest(testDataRoot) {

    lateinit var codeStyleManager: CodeStyleManager

    @JvmField
    @Parameterized.Parameter(0)
    var inFile: File? = null

    @JvmField
    @Parameterized.Parameter(1)
    var outFile: File? = null

    companion object {
        fun getInAndOutArray(
            cls: KFunction<TransformationsTest>,
            resourcesRootName: String = resourcesRoot,
            inFormat: TestFileFormat = TestFileFormat("in", FileUtil.Extension.JAVA, Type.Input),
            outFormat: TestFileFormat = TestFileFormat("out", FileUtil.Extension.JAVA, Type.Output)
        ): List<Array<File>> {
            val inAndOutFilesMap = FileTestUtil.getInAndOutFilesMap(
                getResourcesRootPath(cls, resourcesRootName),
                inFormat = inFormat,
                outFormat = outFormat
            )
            return inAndOutFilesMap.entries.map { (inFile, outFile) -> arrayOf(inFile, outFile) }
        }
    }

    @Before
    override fun mySetUp() {
        super.setUp()
        codeStyleManager = CodeStyleManager.getInstance(project)
    }

    protected fun assertCodeTransformation(
        inFile: File,
        outFile: File,
        transformation: (PsiElement, Boolean) -> Unit
    ) {
        LOG.info("The current input file is: ${inFile.path}")
        LOG.info("The current output file is: ${outFile.path}")
        val expectedSrc = getPsiFile(outFile).text
        LOG.info("The expected code is:\n$expectedSrc")
        val psiInElement = getPsiFile(inFile)
        ApplicationManager.getApplication().invokeAndWait {
            transformation(psiInElement, true)
            PsiTestUtil.checkFileStructure(psiInElement)
        }
        val actualSrc = psiInElement.text
        LOG.info("The actual code is:\n$actualSrc")
        assertEquals(expectedSrc, actualSrc)
    }

    private fun getPsiFile(file: File, toReformatCode: Boolean = true): PsiFile {
        val psi = myFixture.configureByFile(file.path)
        if (toReformatCode) {
            formatPsiFile(psi)
        }
        return psi
    }

    private fun formatPsiFile(psi: PsiElement) {
        WriteCommandAction.runWriteCommandAction(project) { // reformat the expected file
            codeStyleManager.reformat(psi)
        }
    }
}
