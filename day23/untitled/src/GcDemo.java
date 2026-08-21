/**
 * W4 Day 3 · 垃圾回收（GC）入门演示
 *
 * 运行方式（在 IDEA 里点 Run 也行，但要看到 GC 日志建议命令行）：
 *   javac GcDemo.java
 *   java -Xmx64m -verbose:gc GcDemo
 *
 *  -Xmx64m     把堆上限压到 64MB（模拟内存紧张）
 *  -verbose:gc 打印 GC 日志：看 [GC ...] 和 [Full GC ...]
 *
 * 观察点：
 *  1. 循环里每轮 new 一个 5MB 数组，用完就丢（没人引用 → 垃圾）
 *  2. 20 个 × 5MB = 100MB，远超 64MB 堆 → 装不下时 JVM 自动 GC
 *  3. GC 日志里出现 [Full GC ...] → 老年代回收（看后面 Day 3 讲解）
 */
public class GcDemo {
    public static void main(String[] args) {
        System.out.println("=== 堆上限 64MB，循环创建 20 个 5MB 对象 ===");
        for (int i = 1; i <= 20; i++) {
            byte[] big = new byte[5 * 1024 * 1024]; // 每轮 5MB，用完即弃
            System.out.println("第 " + i + " 个 5MB 对象创建完毕");
        }
        System.out.println("=== 循环结束：之前 20 个对象都没人引用了，全是垃圾 ===");

        // 演示 2：手动把引用置空 + System.gc() 提示回收（只是"建议"，不保证立即执行）
        System.out.println("--- 演示 2：置 null + System.gc() ---");
        byte[] data = new byte[10 * 1024 * 1024];
        data = null;       // 不再有任何引用指向它 → 变成垃圾
        System.gc();       // 建议 JVM 执行垃圾回收
        System.out.println("已发出 GC 请求（是否立即回收由 JVM 决定）");
    }
}
