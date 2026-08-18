public class Bank {
    private int count = 1000;
    public synchronized boolean sell(int money) throws InterruptedException {
        if (count > money) {
            Thread.sleep(100);
            count -= money;
            System.out.println(Thread.currentThread().getName()
                    + " 余额剩 " + money);
            return true;
        }
        System.out.println(Thread.currentThread().getName()
                + " 余额不足，拒绝取款");
        return  false;
    }
    public static  void main(String[] args) {
        Bank bank = new Bank();
        for (int i = 1; i <= 2; i++) {
            new Thread(() -> {
                try {
                    while (bank.sell(600)) {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "窗口" + i).start();
        }
    }
}
