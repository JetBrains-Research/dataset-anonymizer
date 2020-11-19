public class c1 {

    public static void main(String[] main_p1) {

        i1 main_v1;
        main_v1 = (main_l1_p1, main_l1_p2) -> main_l1_p1 + main_l1_p2;

        int main_v2 = main_v1.i1_f1(10, 20);
        System.out.println(main_v2); //30
    }
}

interface i1 {
    int i1_f1(int i1_f1_p1, int i1_f1_p2);
}