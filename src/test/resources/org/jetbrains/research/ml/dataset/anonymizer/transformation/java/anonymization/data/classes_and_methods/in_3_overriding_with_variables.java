public abstract class Test3_0 {

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static void main() {
    }

    public int calculate(int x, int y) {
        return 0;
    }

    abstract public void test(int x, int y);
}

class Test3_1 extends Test3_0 {

    @Override
    public void test(int x, int y) {

    }
}

interface Operationable{
    int calculate(int x, int y);
}