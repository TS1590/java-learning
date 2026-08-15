public class ThreadDemo {
    public static void main(String[] args) {
        // 方式一：继承 Thread（了解即可）
        Thread t1 = new MyThread();
        t1.start();   // 启动线程！注意是 start() 不是 run()

        // 方式二：实现 Runnable（重点掌握，面试推荐）
        Runnable task = new MyRunnable();
        Thread t2 = new Thread(task);
        t2.start();

        // 主线程继续干活
        for (int i = 1; i <= 5; i++) {
            System.out.println("主线程：第 " + i + " 次");
        }
    }
}

// 方式一：继承 Thread，重写 run()
class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("MyThread 线程：第 " + i + " 次");
        }
    }
}

// 方式二：实现 Runnable，重写 run()（推荐！Java 单继承，继承 Thread 太浪费）
class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("MyRunnable 线程：第 " + i + " 次");
        }
    }
}
