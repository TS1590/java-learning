public class PrintNum {
    public static void main(String[] args) {
        Nums nums = new Nums();
        new Thread(new ThreadA(nums), "A").start();
        new Thread(new ThreadB(nums), "B").start();
    }
}

class Nums {
    private int num = 1;   // 下一个要打印的数字（共享，就这一个计数器）

    // A 线程：打印奇数
    public synchronized void printOdd() throws InterruptedException {
        while (num % 2 == 0) { wait(); }   // 轮到偶数了 → 奇数线程让位等待
        System.out.println("A: " + num);
        num++;
        notifyAll();
    }

    // B 线程：打印偶数
    public synchronized void printEven() throws InterruptedException {
        while (num % 2 == 1) { wait(); }   // 轮到奇数了 → 偶数线程让位等待
        System.out.println("B: " + num);
        num++;
        notifyAll();
    }
}

class ThreadA implements Runnable {
    private Nums nums;

    public ThreadA(Nums nums) { this.nums = nums; }

    public void run() {
        for (int i = 1; i <= 5; i++) {      // 打 5 个奇数：1 3 5 7 9
            try { nums.printOdd(); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}

class ThreadB implements Runnable {
    private Nums nums;

    public ThreadB(Nums nums) { this.nums = nums; }

    public void run() {
        for (int i = 1; i <= 5; i++) {      // 打 5 个偶数：2 4 6 8 10
            try { nums.printEven(); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
