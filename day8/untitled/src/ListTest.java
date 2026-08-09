import java.util.ArrayList;

public class ListTest {
    public static void main(String[] args) {
        // 1. 创建：像"会自动变长的数组"
        ArrayList<String> names = new ArrayList<>();

        // 2. 加：add(元素)
        names.add("小明");
        names.add("小红");
        names.add("小刚");
        names.add("小丽");
        names.add("小华");

        // 3. 取：get(下标)，下标从 0 开始
        System.out.println(names.get(0));   // 小明

        // 4. 改：set(下标, 新值)
        names.set(1, "小红改名");

        // 5. 删：remove(下标)
        names.remove(3);                    // 删掉小丽

        // 6. 个数：size()（不是 length！）
        System.out.println(names.size());   // 4

        // 7. 遍历方式一：普通 for（用下标）
        for (int i = 0; i < names.size(); i++) {
            System.out.println("第" + i + "个: " + names.get(i));
        }

        // 8. 遍历方式二：增强 for（直接拿元素，不要下标）
        for (String name : names) {
            System.out.println(name);
        }
    }
}
