public class Bank {
    private int count = 1000;
    public synchronized boolean withdraw(int money) throws InterruptedException {
        if (count >= money) {      // 边界：余额恰好等于取款额也要允许（如 600 取 600）
            Thread.sleep(100);
            count -= money;
            System.out.println(Thread.currentThread().getName()
                    + " 余额剩 " + count);
            return true;
        }
        System.out.println(Thread.currentThread().getName()
                + " 余额不足，拒绝取款");
        return false;
    }
    public static void main(String[] args) {
        Bank bank = new Bank();
        for (int i = 1; i <= 2; i++) {
            new Thread(() -> {
                try {
                    while (bank.withdraw(600)) {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "窗口" + i).start();
        }
    }
}
