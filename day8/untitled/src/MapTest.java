import java.util.HashMap;   // 引入 HashMap 类

public class MapTest {
    public static void main(String[] args) {
        // 1. 创建：键->值 成对存，像"学号 -> 名字"
        HashMap<String, String> map = new HashMap<>();

        // 2. 放数据：put(键, 值)
        map.put("001", "张三");
        map.put("002", "李四");
        map.put("003", "王五");

        // 3. 取数据：get(键)
        System.out.println(map.get("002"));  // 输出：李四

        // 4. 键不存在时，get 返回 null
        System.out.println(map.get("999"));  // 输出：null

        // 5. 有多少对
        System.out.println(map.size());      // 输出：3

        // 6. 遍历：先拿所有键，再逐个取值
        for (String key : map.keySet()) {
            System.out.println(key + " -> " + map.get(key));
        }
    }
}
