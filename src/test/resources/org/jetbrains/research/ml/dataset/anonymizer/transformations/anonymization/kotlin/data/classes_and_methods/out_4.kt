abstract class c1 {
    abstract fun c1_f1(c1_f1_p1: Int, c1_f1_p2: Int)
}

open class c2 : c1() {
    override fun c1_f1(c1_f1_p1: Int, c1_f1_p2: Int) {}
}

abstract class c3 : c2() {
    override fun c1_f1(c1_f1_p1: Int, c1_f1_p2: Int) {}
    abstract fun c3_f1(c3_f1_p1: Int, c3_f1_p2: Int)
}

class c4 : c3() {
    override fun c1_f1(c1_f1_p1: Int, c1_f1_p2: Int) {}
    override fun c3_f1(c3_f1_p1: Int, c3_f1_p2: Int) {}
}
