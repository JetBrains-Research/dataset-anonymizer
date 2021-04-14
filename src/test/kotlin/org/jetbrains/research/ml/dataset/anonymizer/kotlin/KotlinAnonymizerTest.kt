package org.jetbrains.research.ml.dataset.anonymizer.kotlin

import org.jetbrains.research.ml.dataset.anonymizer.util.AnonymizerTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class KotlinAnonymizerTest : AnonymizerTest(getResourcesRootPath(::KotlinAnonymizerTest)) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: ({0}, {1})")
        fun getTestData() = getInAndOutArray(::KotlinAnonymizerTest)
    }

    @Test
    fun kotlinAnonymizerTest() {
        val anonymizer = KotlinAnonymizer(myFixture.project, testDataPath)
        assertCodeAnonymization(inFile!!, outFile!!, anonymizer, toCreateTmpFiles = true)
    }
}
