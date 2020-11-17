package org.jetbrains.research.ml.dataset.anonymizer.python

import org.jetbrains.research.ml.dataset.anonymizer.util.AnonymizerTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class PythonAnonymizerTest : AnonymizerTest(getResourcesRootPath(::PythonAnonymizerTest)) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: ({0}, {1})")
        fun getTestData() = getInAndOutArray(::PythonAnonymizerTest)
    }

    @Test
    fun pythonAnonymizerTest(){
        val anonymizer = PythonAnonymizer(myFixture.project, testDataPath)
        assertCodeAnonymization(inFile!!, outFile!!, anonymizer)
    }

}
