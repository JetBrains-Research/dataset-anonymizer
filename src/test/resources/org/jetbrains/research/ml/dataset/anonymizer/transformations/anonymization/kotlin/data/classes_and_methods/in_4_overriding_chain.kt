abstract class Test4_0 {
    abstract fun test(x: Int, y: Int)
}

open class Test4_1 : Test4_0() {
    override fun test(x: Int, y: Int) {}
}

abstract class Test4_2 : Test4_1() {
    override fun test(x: Int, y: Int) {}
    abstract fun test1(x: Int, y: Int)
}

class Test4_3 : Test4_2() {
    override fun test(x: Int, y: Int) {}
    override fun test1(x: Int, y: Int) {}
}
