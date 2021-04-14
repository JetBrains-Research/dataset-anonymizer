package org.jetbrains.research.ml.dataset.anonymizer.util

import junit.framework.TestCase
import krangl.DataFrame
import krangl.StringCol
import krangl.readCSV
import org.jetbrains.research.ml.dataset.anonymizer.Anonymizer
import org.junit.Ignore
import org.junit.runners.Parameterized
import java.io.File
import kotlin.reflect.KFunction

@Ignore
open class AnonymizerTest(testDataRoot: String) : ParametrizedBaseTest(testDataRoot) {

    companion object {
        fun getInAndOutArray(
            cls: KFunction<AnonymizerTest>,
            resourcesRootName: String = resourcesRoot
        ): List<Array<File>> {
            val inAndOutFilesMap = FileTestUtil.getInAndOutFilesMap(getResourcesRootPath(cls, resourcesRootName))
            return inAndOutFilesMap.entries.map { (inFile, outFile) -> arrayOf(inFile, outFile) }
        }
    }

    @JvmField
    @Parameterized.Parameter(0)
    var inFile: File? = null

    @JvmField
    @Parameterized.Parameter(1)
    var outFile: File? = null

    private fun getFragments(df: DataFrame): List<String> {
        if (Column.FRAGMENT.key !in df.cols.map { it.name }) {
            return emptyList()
        }
        return (df[Column.FRAGMENT.key] as StringCol).values.mapNotNull { it }
    }

    // Usually we have to create psi file from fixture to correct find all references.
    // But for Kotlin we have to create temporary files instead of it
    protected fun assertCodeAnonymization(
        inFile: File,
        outFile: File,
        anonymizer: Anonymizer,
        toCreateTmpFiles: Boolean = false
    ) {
        LOG.info("The current input file is: ${inFile.path}")
        LOG.info("The current output file is: ${outFile.path}")
        val expectedDf = DataFrame.readCSV(outFile.path)
        LOG.info("The expected df is:\n$expectedDf")
        val fixture = if (toCreateTmpFiles) {
            null
        } else {
            myFixture
        }
        val actualDf = anonymizer.anonymizeCsvFile(inFile.path, fixture)
        // We can compare only <fragment> column from df
        TestCase.assertEquals(getFragments(expectedDf), getFragments(actualDf))
    }
}
