public class ThreadLife {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("子线程：第 " + i + " 次");
                try { Thread.sleep(500); } catch (InterruptedException e) { }
            }
        });
        t.start();

        t.join();   // 主线程在这等 t 跑完，再继续往下走
        System.out.println("主线程：子线程跑完了，我继续");
    }
}
