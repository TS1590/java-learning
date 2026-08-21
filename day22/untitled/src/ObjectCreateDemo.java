/**
 * W4 Day 2 · new 一个对象的完整过程（高频面试题，必背）
 *
 * 标准 5 步：
 *  1 类加载检查：类还没加载就先加载（静态代码块只执行一次）
 *  2 分配内存：在堆里划一块空间
 *  3 零值初始化：int=0 / boolean=false / 引用=null（先给默认值）
 *  4 设置对象头：哈希码、GC 分代年龄、锁状态（看不见但真实存在）
 *  5 执行构造方法：把默认值换成真实值
 */
public class ObjectCreateDemo {

    // 第 1 步：类加载时执行，整个程序只执行一次
    static {
        System.out.println("[第1步] 类加载检查完成（静态代码块只跑一次）");
    }

    int age;     // 第 3 步会给它零值 0
    String name; // 第 3 步会给它零值 null

    // 实例代码块：new 时、构造方法之前执行 → 正好能看到零值
    {
        System.out.println("[第3步后] 实例代码块：此时 age 还是零值 = " + age);
    }

    public ObjectCreateDemo(String n, int a) {
        System.out.println("[第5步] 构造方法执行：把零值换成真实值");
        name = n;
        age = a;
    }

    public static void main(String[] args) {
        // 第2步（分配内存）发生在 new 的一瞬间——对象在堆里占一块地，等 GC 来回收
        System.out.println("--- new 第1个对象 ---");
        ObjectCreateDemo d1 = new ObjectCreateDemo("张三", 21);
        System.out.println("--- new 第2个对象 ---");
        ObjectCreateDemo d2 = new ObjectCreateDemo("李四", 22);
        System.out.println("--- new 第3个对象 ---");
        ObjectCreateDemo d3 = new ObjectCreateDemo("王五", 23);

        // 关键观察：静态代码块只打印 1 次（类只加载 1 次），实例代码块打印 3 次（每 new 一次执行一次）
        System.out.println("观察：静态块只出现 1 次 → 类只加载 1 次；实例块出现 3 次 → 每次 new 都重新执行");
        System.out.println("结果：d1=" + d1.name + "/" + d1.age + "，d2=" + d2.name + "/" + d2.age + "，d3=" + d3.name + "/" + d3.age);

        // 第4步：对象头里存着 hashCode（还有 GC 分代年龄、锁状态），每个对象各不相同
        System.out.println("三个对象的 hashCode 各不相同：" + d1.hashCode() + " / " + d2.hashCode() + " / " + d3.hashCode());
    }
}
