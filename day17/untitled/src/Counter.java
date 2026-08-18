public class Counter {
    private int count = 0;

    // 同步方法：count++ 三步（读-改-写）保证原子执行
    public synchronized void increment() {
        count++;
    }

    /* 同步代码块（效果一样）
    public void increment() {
        synchronized (this) {
            count++;
        }
    }
    */

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        Counter c = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                c.increment();
            }
        }, "线程1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                c.increment();
            }
        }, "线程2");

        t1.start();
        t2.start();

        t1.join();   // 主线程等 t1 跑完（Day 2 学的 join）
        t2.join();   // 主线程等 t2 跑完

        System.out.println("最终 count = " + c.getCount());
    }
}
