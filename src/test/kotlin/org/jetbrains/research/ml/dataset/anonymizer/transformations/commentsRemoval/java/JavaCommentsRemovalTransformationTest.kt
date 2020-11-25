package org.jetbrains.research.ml.dataset.anonymizer.transformations.commentsRemoval.java

import org.jetbrains.research.ml.dataset.anonymizer.transformations.util.TransformationsTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class JavaCommentsRemovalTransformationTest :
    TransformationsTest(getResourcesRootPath(::JavaCommentsRemovalTransformationTest)) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: ({0}, {1})")
        fun getTestData() = getInAndOutArray(::JavaCommentsRemovalTransformationTest)
    }

    @Test
    fun testForwardTransformation() {
        assertCodeTransformation(inFile!!, outFile!!) { psiTree, toStoreMetadata ->
            JavaCommentsRemovalTransformation.apply(psiTree, toStoreMetadata)
        }
    }
}
