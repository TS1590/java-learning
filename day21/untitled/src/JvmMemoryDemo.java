/**
 * W4 Day 1 · JVM 内存模型示例
 * 看懂每一行代码里的"东西"存到内存的哪个区域
 *
 * 区域速记（4 块够用）：
 *  栈      → 方法运行时产生的局部变量、引用（方法结束就弹栈）
 *  堆      → new 出来的对象、数组（大家共享，垃圾回收管它）
 *  方法区   → 类信息、静态变量（JDK8+ 叫元空间 Metaspace）
 *  程序计数器 → 每个线程记录自己执行到哪一行（先不用管它）
 */
public class JvmMemoryDemo {

    // ① 静态变量 → 方法区（元空间），所有对象共享一份
    static String school = "温商院";

    // ② 实例变量 → 堆（跟着 new 出来的对象走）
    String name;

    public static void main(String[] args) {
        // ③ 局部变量 → 栈（main 方法的栈帧里）
        int age = 21;
        double score = 88.5;

        // ④ new 出来的对象 → 堆；stu 这个引用变量 → 栈
        JvmMemoryDemo stu = new JvmMemoryDemo();
        stu.name = "张三";

        // ⑤ 方法调用 → 新栈帧压栈；方法结束 → 弹栈（栈内存自动释放）
        greet(stu);

        // ⑥ 字符串字面量 → 字符串常量池（JDK7+ 池子在堆里）
        String s1 = "abc";
        String s2 = new String("abc");

        System.out.println("栈上: age=" + age + ", score=" + score);
        System.out.println("s1 和 s2 是同一个对象吗? " + (s1 == s2));
    }

    static void greet(JvmMemoryDemo s) {
        System.out.println("你好，" + s.name + "，来自 " + school);
    }
}
