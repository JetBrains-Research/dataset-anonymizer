class Test1_0 {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    fun main() {
        val a = 5
        val b = a
    }

}

internal interface Operationable {
    fun calculate(x: Int, y: Int): Int
}

