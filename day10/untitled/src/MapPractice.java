import java.util.HashMap;

public class MapPractice {
    public static void main(String[] args) {
        // 1. 创建：水果 -> 价格
        HashMap<String, Integer> map = new HashMap<>();
        map.put("苹果", 8);
        map.put("香蕉", 3);
        map.put("葡萄", 15);

        // 2. 遍历打印：先拿所有键，再逐个取值（Day 1 就学过）
        for (String key : map.keySet()) {
            System.out.println(key + ": " + map.get(key) + " 元");
        }

        // 3. 修改苹果价格：对同一个 key 再 put 一次 = 覆盖旧值！
        map.put("苹果", 12);

        // 4. 删除葡萄：remove(键)
        map.remove("葡萄");

        // 5. 打印最终 size
        System.out.println("还剩 " + map.size() + " 种水果");
    }
}
