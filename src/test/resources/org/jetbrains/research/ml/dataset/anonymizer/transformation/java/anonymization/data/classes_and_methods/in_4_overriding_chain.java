public abstract class Test4_0  {
    abstract public void test(int x, int y);
}

public class Test4_1  extends Test4_0  {
    @Override
    public void test(int x, int y) {

    }
}

public class Test4_2 extends Test4_1  {
    @Override
    public void test(int x, int y) {

    }

    abstract public void test1(int x, int y);
}

public class Test4_3 extends Test4_2 {
    @Override
    public void test(int x, int y) {

    }

    @Override
    public void test1(int x, int y) {

    }
}