import java.util.Random;
import java.util.TreeSet;

public class SetSort {
    public static void main(String[] args) {
        TreeSet<String> treeSet = new TreeSet<>();
        Random[] randomArray = new Random[20];
        for (int i = 0; i < randomArray.length; i++) {
            randomArray[i] = new Random();
            treeSet.add(String.valueOf(randomArray[i]));
        }
        System.out.println(treeSet);
    }
}
