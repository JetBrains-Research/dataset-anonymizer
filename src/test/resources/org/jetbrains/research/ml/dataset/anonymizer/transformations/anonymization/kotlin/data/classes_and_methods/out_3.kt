public abstract class c1 : i1 {

    override fun equals(equals_p1: Any?): Boolean {
        return super.equals(equals_p1)
    }

    fun main() {

    }


    override fun i1_f1(i1_f1_p1: Int, i1_f1_p2: Int): Int {
        return 0
    }

    abstract fun c1_f1(c1_f1_p1: Int, c1_f1_p2: Int)
}

class c2 : c1 {
    override fun c1_f1(c1_f1_p1: Int, c1_f1_p2: Int) {

    }
}

interface i1 {
    fun i1_f1(i1_f1_p1: Int, i1_f1_p2: Int): Int
}
