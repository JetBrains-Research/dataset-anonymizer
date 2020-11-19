class Test1_0 {
    override fun equals(obj: Any?): Boolean {
        return super.equals(obj)
    }

    companion object {
        fun main() {
            val a = 5
            val b = a
        }
    }
}

internal interface Operationable {
    fun calculate(x: Int, y: Int): Int
}
