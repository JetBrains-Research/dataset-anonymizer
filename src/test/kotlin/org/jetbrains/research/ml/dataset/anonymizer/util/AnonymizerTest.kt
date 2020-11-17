package org.jetbrains.research.ml.dataset.anonymizer.util

import junit.framework.TestCase
import krangl.DataFrame
import krangl.readCSV
import org.jetbrains.research.ml.dataset.anonymizer.Anonymizer
import org.jetbrains.research.ml.dataset.anonymizer.util.FileUtil.handleCsvFile
import org.junit.Ignore
import org.junit.runners.Parameterized
import java.io.File
import kotlin.reflect.KFunction

@Ignore
open class AnonymizerTest(testDataRoot: String) : ParametrizedBaseTest(testDataRoot) {

    companion object {
        fun getInAndOutArray(
            cls: KFunction<AnonymizerTest>,
            resourcesRootName: String = resourcesRoot,
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

    protected fun assertCodeAnonymization(inFile: File, outFile: File, anonymizer: Anonymizer){
        LOG.info("The current input file is: ${inFile.path}")
        LOG.info("The current output file is: ${outFile.path}")
        val expectedDf = DataFrame.readCSV(outFile.path)
        LOG.info("The expected df is:\n$expectedDf")
        val actualDf = handleCsvFile(inFile.path, anonymizer)
        // TODO: can we compare the dfs better?
        TestCase.assertEquals(expectedDf.toString(), actualDf.toString())
    }
}
