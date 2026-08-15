import java.util.ArrayList;

public class ListExercise {
    public static void main(String[] args) {
        ArrayList<String> List = new ArrayList<>();
        List.add("Java");
        List.add("Python");
        List.add("C++");
        List.add("Java");
        List.add("Go");
        List.add("Python");

        List.removeIf(s -> s.equals("C++"));

        int count = 0;
        for (String s : List) {
            if (s.equals("Java")) {
                count++;
            }
        }
        System.out.println(count);

        List.add("Java");

        int count1 = 0;
        for (String s : List) {
            if (s.equals("Java")) {
                count1++;
            }
        }
        System.out.println(count1);

        for(String s: List){
            System.out.println(s);
        }
    }

}
