package org.jetbrains.research.ml.dataset.anonymizer.transformation.kotlin.comentsRemoval

import org.jetbrains.research.ml.dataset.anonymizer.transformation.util.TransformationsTest
import org.jetbrains.research.ml.dataset.anonymizer.util.Extension
import org.jetbrains.research.ml.dataset.anonymizer.util.TestFileFormat
import org.jetbrains.research.ml.dataset.anonymizer.util.Type
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class KotlinCommentsRemovalTransformationTest :
    TransformationsTest(getResourcesRootPath(::KotlinCommentsRemovalTransformationTest)) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: ({0}, {1})")
        fun getTestData() = getInAndOutArray(
            ::KotlinCommentsRemovalTransformationTest,
            inFormat = TestFileFormat("in", Extension.KOTLIN, Type.Input),
            outFormat = TestFileFormat("out", Extension.KOTLIN, Type.Output)
        )
    }

    @Test
    fun testForwardTransformation() {
        assertCodeTransformation(inFile!!, outFile!!) { psiTree, toStoreMetadata ->
            KotlinCommentsRemovalTransformation.apply(psiTree, toStoreMetadata)
        }
    }
}
