/**
 * Day 3 线程同步 synchronized —— 第 1 步：不加锁版本
 * 先跑这个版本，观察输出：会出现负数票 / 重复票！
 */
public class SellTickets {
    private int tickets = 100;   // 共 100 张票

    // 卖票：有票才卖，返回是否卖成功
    public boolean sell() throws InterruptedException {
        if (tickets > 0) {
            Thread.sleep(10);          // 模拟卖票耗时：查库存 -> 下单 -> 扣减
            tickets--;                 // 扣减库存
            System.out.println(Thread.currentThread().getName()
                    + " 卖出一张，剩 " + tickets);
            return true;
        }
        return false;                  // 没票了
    }

    public static void main(String[] args) {
        SellTickets st = new SellTickets();

        // 开 3 个窗口（线程）同时卖这 100 张票
        for (int i = 1; i <= 3; i++) {
            new Thread(() -> {
                try {
                    while (st.sell()) {     // 一直卖，直到没票
                        Thread.sleep(1);    // 卖完一张歇一下，让别的窗口也来抢
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "窗口" + i).start();
        }
    }
}
