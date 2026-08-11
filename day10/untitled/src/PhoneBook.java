import java.util.HashMap;

public class PhoneBook {
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();

        map.put("小明","11111111");
        map.put("小红","11111112");
        map.put("小亮","11111113");

        for(String s: map.keySet()){
            System.out.println(map.get(s));
        }

        System.out.println(map.get("小明"));

        map.remove("小亮");

        System.out.println(map.size());
    }
}
