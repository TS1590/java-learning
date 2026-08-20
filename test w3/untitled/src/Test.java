// Test.java —— 验证单例：100 个线程同时抢，只允许创建一个对象

public class Test {
    public static void main(String[] args) throws InterruptedException {
        // 100 个线程同时调 getInstance()，模拟并发竞争
        Thread[] threads = new Thread[100];
        final Singleton[] results = new Singleton[100];

        for (int i = 0; i < 100; i++) {
            int idx = i;
            threads[i] = new Thread(() -> {
                results[idx] = Singleton.getInstance();   // 抢同一个单例
            });
            threads[i].start();
        }

        // 等所有线程跑完（复习 Day 2 的 join）
        for (Thread t : threads) {
            t.join();
        }

        // 验证：100 个结果必须全是同一个对象
        boolean allSame = true;
        for (int i = 1; i < results.length; i++) {
            if (results[i] != results[0]) {
                allSame = false;
                break;
            }
        }
        System.out.println("100 个线程拿到同一对象？" + (allSame ? "是 ✅" : "否 ❌"));
        System.out.println("对象地址：" + results[0]);
    }
}
