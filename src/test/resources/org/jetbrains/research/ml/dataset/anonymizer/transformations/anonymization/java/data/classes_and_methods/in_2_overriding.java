public class Test2_0 implements Operationable {

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static void main() {
    }

    @Override
    public int calculate(int x, int y) {
        return 0;
    }
}

class Test2_1 extends Test2_0 {

}

interface Operationable {
    int calculate(int x, int y);
}