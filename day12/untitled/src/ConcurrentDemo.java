import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentDemo {
    public static void main(String[] args) {
        // 多线程并发场景：用 ConcurrentHashMap 替代 HashMap
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("商品A", 100);
        map.put("商品B", 50);
        System.out.println("商品A 库存：" + map.get("商品A"));
    }
}
