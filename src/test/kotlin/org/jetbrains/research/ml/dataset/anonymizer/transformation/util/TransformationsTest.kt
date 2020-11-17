package org.jetbrains.research.ml.dataset.anonymizer.transformation.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import org.jetbrains.research.ml.dataset.anonymizer.util.*
import org.jetbrains.research.ml.dataset.anonymizer.util.FileTestUtil.content
import org.junit.Ignore
import org.junit.runners.Parameterized
import java.io.File
import kotlin.reflect.KFunction

@Ignore
open class TransformationsTest(testDataRoot: String) : ParametrizedBaseTest(testDataRoot) {

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

    protected fun assertCodeTransformation(
        inFile: File,
        outFile: File,
        transformation: (PsiElement, Boolean) -> Unit
    ) {
        LOG.info("The current input file is: ${inFile.path}")
        LOG.info("The current output file is: ${outFile.path}")
        val expectedSrc = outFile.content
        LOG.info("The expected code is:\n$expectedSrc")
        val psiInElement = myFixture.configureByFile(inFile.path)
        ApplicationManager.getApplication().invokeAndWait {
            transformation(psiInElement, true)
//            PsiTestUtil.checkFileStructure(psiInElement)
        }
        val actualSrc = psiInElement.text
        LOG.info("The actual code is:\n$actualSrc")
        assertEquals(expectedSrc, actualSrc)
    }
}
