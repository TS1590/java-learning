// DeadLock.java —— Day 5 死锁复现
// 场景：两把锁 A/B。线程1拿A等B，线程2拿B等A → 互相等 → 程序卡死
public class DeadLock {
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        new Thread(new Worker1(), "线程1").start();
        new Thread(new Worker2(), "线程2").start();
        System.out.println("main 等 3 秒，看程序会不会自己结束...");
    }

    // 线程 1：先拿 A，再拿 B
    static class Worker1 implements Runnable {
        public void run() {
            synchronized (LOCK_A) {
                System.out.println(Thread.currentThread().getName() + " 拿到锁A，等锁B...");
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                synchronized (LOCK_B) {
                    System.out.println(Thread.currentThread().getName() + " 拿到锁B，干活");
                }
            }
        }
    }

    // 线程 2：修复！改成和线程1 一样的加锁顺序（先 A 后 B）→ 死锁解除
    static class Worker2 implements Runnable {
        public void run() {
            synchronized (LOCK_A) {
                System.out.println(Thread.currentThread().getName() + " 拿到锁A，等锁B...");
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                synchronized (LOCK_B) {
                    System.out.println(Thread.currentThread().getName() + " 拿到锁B，干活");
                }
            }
        }
    }
}
