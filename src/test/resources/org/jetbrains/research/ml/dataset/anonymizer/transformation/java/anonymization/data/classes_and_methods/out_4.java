public abstract class c1 {
    abstract public void c1_f1(int c1_f1_p1, int c1_f1_p2);
}

public class c2 extends c1 {
    @Override
    public void c1_f1(int c1_f1_p1, int c1_f1_p2) {

    }
}

public class c3 extends c2 {
    @Override
    public void c1_f1(int c1_f1_p1, int c1_f1_p2) {

    }

    abstract public void c3_f1(int c3_f1_p1, int c3_f1_p2);
}

public class c4 extends c3 {
    @Override
    public void c1_f1(int c1_f1_p1, int c1_f1_p2) {

    }

    @Override
    public void c3_f1(int c3_f1_p1, int c3_f1_p2) {

    }
}