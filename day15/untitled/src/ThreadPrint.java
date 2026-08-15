public class ThreadPrint {
    public static void main(String[] args) {
        Runnable r = new MyRunnable1();
        Thread t1 = new Thread(r);
        t1.start();
        for (int i = 1; i <= 5; i++) {
            System.out.println("主线程：第 " + i + " 次");
        }
    }
}
class MyRunnable1 implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(i);
        }
    }
}
