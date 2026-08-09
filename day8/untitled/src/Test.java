import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<String> List = new ArrayList<>();

        List.add("同学1");
        List.add("同学2");
        List.add("同学3");
        List.add("同学4");
        List.add("同学5");

        System.out.println("第 3 个同学叫" + List.get(2));
    }
}
