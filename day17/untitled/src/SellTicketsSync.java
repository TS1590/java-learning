/**
 * Day 3 线程同步 synchronized —— 第 2 步：加锁版本
 * 关键点：synchronized 锁的是 this（当前这个实例对象）
 *         3 个线程用的是同一个 SellTicketsSync 对象，互斥才生效
 */
public class SellTicketsSync {
    private int tickets = 100;

    // 方式一：同步方法（锁 = this）
    public synchronized boolean sell() throws InterruptedException {
        if (tickets > 0) {
            Thread.sleep(10);
            tickets--;
            System.out.println(Thread.currentThread().getName()
                    + " 卖出一张，剩 " + tickets);
            return true;
        }
        return false;
    }

    /* 方式二：同步代码块（效果一样，锁也是 this）
    public boolean sell() throws InterruptedException {
        synchronized (this) {
            if (tickets > 0) {
                Thread.sleep(10);
                tickets--;
                System.out.println(Thread.currentThread().getName()
                        + " 卖出一张，剩 " + tickets);
                return true;
            }
        }
        return false;
    }
    */

    public static void main(String[] args) {
        SellTicketsSync st = new SellTicketsSync();

        for (int i = 1; i <= 3; i++) {
            new Thread(() -> {
                try {
                    while (st.sell()) {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "窗口" + i).start();
        }
    }
}
