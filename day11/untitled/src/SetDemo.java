import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class SetDemo {
    public static void main(String[] args) {
        // 1. HashSet：存 5 个名字（故意存重复的"小明"两次）
        HashSet<String> set = new HashSet<>();
        set.add("小明");
        set.add("小红");
        set.add("小明");     // 重复！
        set.add("小刚");
        set.add("小丽");
        System.out.println(set.size());   // 猜猜输出几？

        // 2. LinkedHashSet：有序版
        LinkedHashSet<String> Link = new LinkedHashSet<>();
        Link.add("1");
        Link.add("3");
        Link.add("2");
        for(String s: Link){
            System.out.println(s);
        }
        // 3. TreeSet：自动排序版（数字从小到大 / 字母从 a 到 z）
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("a");
        treeSet.add("c");
        treeSet.add("b");
        for(String s: treeSet){
            System.out.println(s);
        }
        // 后两个照葫芦画瓢，各自 add 3 个元素再打印，观察打印顺序
    }
}
