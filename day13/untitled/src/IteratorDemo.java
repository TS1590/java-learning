import java.util.*;

public class IteratorDemo {
    public static void main(String[] args) {
        // ========== 1. 用 Iterator 遍历 List ==========
        List<String> list = new ArrayList<>();
        list.add("苹果");
        list.add("香蕉");
        list.add("西瓜");

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {            // hasNext：还有下一个吗？
            String fruit = it.next();     // next：取下一个（同时指针后移）
            System.out.println(fruit);
        }

        // ========== 2. 边遍历边删除（关键！用 it.remove()） ==========
        List<Integer> nums = new ArrayList<>(Arrays.asList(3, 7, 1, 9, 5));
        Iterator<Integer> it2 = nums.iterator();
        while (it2.hasNext()) {
            int n = it2.next();
            if (n > 5) {
                it2.remove();             // ✅ 删除"刚取出的这个"
            }
        }
        System.out.println("删掉 >5 后：" + nums);   // [3, 1, 5]

        // ========== 3. 错误示范：循环里用 list.remove() 会炸 ==========
        // List<Integer> bad = new ArrayList<>(Arrays.asList(1, 2, 3));
        // for (Integer n : bad) {          // for-each 底层就是 Iterator
        //     bad.remove(n);               // ❌ 抛 ConcurrentModificationException
        // }

        // ========== 4. 增强 for 的本质 ==========
        // for (Integer n : nums) { ... } 完全等价于：
        // Iterator<Integer> it3 = nums.iterator();
        // while (it3.hasNext()) { Integer n = it3.next(); ... }
    }
}
