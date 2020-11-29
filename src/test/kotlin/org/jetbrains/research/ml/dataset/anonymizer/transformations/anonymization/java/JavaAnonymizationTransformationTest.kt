package org.jetbrains.research.ml.dataset.anonymizer.transformations.anonymization.java

import org.jetbrains.research.ml.dataset.anonymizer.transformations.util.TransformationsTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class JavaAnonymizationTransformationTest :
    TransformationsTest(getResourcesRootPath(::JavaAnonymizationTransformationTest)) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: ({0}, {1})")
        fun getTestData() = getInAndOutArray(::JavaAnonymizationTransformationTest)
    }

    @Test
    fun testForwardTransformation() {
        assertCodeTransformation(inFile!!, outFile!!) { psiTree, toStoreMetadata ->
            JavaAnonymizationTransformation.apply(psiTree, toStoreMetadata)
        }
    }
}
