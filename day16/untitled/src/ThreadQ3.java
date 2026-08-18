public class ThreadQ3 {
    public static void main(String[] args) throws InterruptedException {
        Runnable r1 = new Runnable1();
        Thread A = new Thread(r1);
        Runnable r2 = new Runnable2();
        Thread B = new Thread(r2);
        A.start();
        B.start();

        A.join();

        System.out.println("主线程结束");
    }
}
class Runnable1 implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(i);
            try { Thread.sleep(300); } catch (InterruptedException e) { }
        }
    }
}
class Runnable2 implements Runnable {
    @Override
    public void run() {
        for (char c = 'a'; c <= 'e'; c++) {   // 线程 B：打印 a-e（字母）
            System.out.println(c);
            try { Thread.sleep(300); } catch (InterruptedException e) { }
        }
    }
}
