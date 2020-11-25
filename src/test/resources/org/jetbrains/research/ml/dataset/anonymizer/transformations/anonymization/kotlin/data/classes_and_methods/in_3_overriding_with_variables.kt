public abstract class Test3_0 : Operationable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    fun main() {

    }


    override fun calculate(x: Int, y: Int): Int {
        return 0;
    }

    abstract fun test(x: Int, y: Int)
}

class Test3_1 : Test3_0 {
    override fun test(x: Int, y: Int) {

    }
}

interface Operationable {
    fun calculate(x: Int, y: Int): Int
}
