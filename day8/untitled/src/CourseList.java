import java.util.ArrayList;

public class CourseList {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Java");
        list.add("MySQL");
        list.add("Redis");

        System.out.println(list.get(1));

        list.set(1,"数据库");

        list.remove(2);

        int sum = 0;
        for(String s: list){
            sum ++;
            System.out.println(s);
        }
        System.out.println(sum);
    }
}
