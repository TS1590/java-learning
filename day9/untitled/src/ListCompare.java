import java.util.ArrayList;
import java.util.LinkedList;

public class ListCompare {
    public static void main(String[] args) {
        // ========== 第一部分：ArrayList 往头部插 10 万个 ==========
        ArrayList<Integer> arrayList = new ArrayList<>();   // 注意：<>里是 Integer 不是 int，先照抄，为什么明天讲

        long start = System.currentTimeMillis();   // ① 记开始时间（毫秒）

        for (int i = 0; i < 100000; i++) {
            arrayList.add(0, i);    // ② 双参 add(下标, 元素)，下标 0 = 永远插头部
        }

        long end = System.currentTimeMillis();     // ③ 记结束时间
        System.out.println("ArrayList 头部插入耗时: " + (end - start) + " ms");   // ④ 两者相减 = 耗时

        // ========== 第二部分：LinkedList 做同样的事 ==========
        LinkedList<Integer> linkedList = new LinkedList<>();

        start = System.currentTimeMillis();

        // ⑤ 交给你：和上面一模一样写一个 for 循环，往 linkedList 头部插 10 万个
        for (int i = 0; i < 100000; i++) {
            linkedList.add(0, i);
        }

        end = System.currentTimeMillis();
        System.out.println("LinkedList 头部插入耗时: " + (end - start) + " ms");
    }
}
