import java.util.*;

public class SetSort {
    public static void main(String[] args) {
        Random r = new Random();            // 一台机器
        TreeSet<Integer> set = new TreeSet<>();  // 注意：存 Integer，不是 String！
        for (int i = 0; i < 20; i++) {
            set.add(r.nextInt(50) + 1);     // 摇 20 次，每次 1~50
        }
        System.out.println(set);            // TreeSet 天然升序 + 去重
    }
}
