public class PrintNum {
    public static void main(String[] args) {
        Nums nums = new Nums();                       // 共享货架
        new Thread(new ThreadA(nums), "A").start();
        new Thread(new ThreadB(nums), "B").start();
    }
}
class Nums{
    private int num;
    private int count = 0;
    public synchronized void putA(int n) throws InterruptedException {
        while (count % 2 == 0) { wait(); }      // 满 → 等
        num = n;                   // 放第一个空位
        count++;
        System.out.println(count);
        notifyAll();
    }
    public synchronized void putB(int n) throws InterruptedException {
        while (count % 2 != 0) { wait(); }
        num = n;
        count++;
        System.out.println(count);
        notifyAll();
    }
}
class ThreadA implements Runnable {
    private Nums nums;

    public ThreadA(Nums nums) { this.nums = nums; }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            try {
                nums.putA(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ThreadB implements Runnable {
    private Nums nums;

    public ThreadB(Nums nums) { this.nums = nums; }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            try {
                nums.putB(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
