public class Test2_0 : Operationable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other);
    }

    fun main() {

    }


    override fun calculate(x: Int, y: Int): Int {
        return 0;
    }
}

class Test2_1 : Test2_0 {

}

interface Operationable {
    fun calculate(x: Int, y: Int): Int
}
