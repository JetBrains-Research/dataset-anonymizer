public class Test2_0 {

    public static void main(String[] args) {

        ITest2_0 operation;
        operation = (x,y)->x+y;

        int result = operation.calculate(10, 20);
        System.out.println(result); //30
    }
}
interface ITest2_0{
    int calculate(int x, int y);
}