package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.kotlin

import org.jetbrains.research.ml.dataset.anonymizer.transformations.util.TransformationsTest
import org.jetbrains.research.ml.dataset.anonymizer.util.FileUtil
import org.jetbrains.research.ml.dataset.anonymizer.util.TestFileFormat
import org.jetbrains.research.ml.dataset.anonymizer.util.Type
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class KotlinAnonymizationTransformationTest :
    TransformationsTest(getResourcesRootPath(::KotlinAnonymizationTransformationTest)) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: ({0}, {1})")
        fun getTestData() = getInAndOutArray(
            ::KotlinAnonymizationTransformationTest,
            inFormat = TestFileFormat("in", FileUtil.Extension.KOTLIN, Type.Input),
            outFormat = TestFileFormat("out", FileUtil.Extension.KOTLIN, Type.Output)
        )
    }

    @Test
    fun testForwardTransformation() {
        assertCodeTransformation(inFile!!, outFile!!) { psiTree, toStoreMetadata ->
            KotlinAnonymizationTransformation.apply(psiTree, toStoreMetadata)
        }
    }
}
