// VolatileDemo.java —— Day 5 volatile 可见性演示
// 场景：主线程改 flag，子线程循环检测。volatile 保证子线程立刻看到变化
public class VolatileDemo {
    private static volatile boolean flag = false;   // volatile 关键字（重点）

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Checker(), "检测线程").start();

        Thread.sleep(2000);        // 主线程睡 2 秒
        flag = true;               // 修改标志
        System.out.println("主线程把 flag 改成 true");
        Thread.sleep(100);
        System.out.println("程序结束");
    }

    // 检测线程：死循环检查 flag
    static class Checker implements Runnable {
        public void run() {
            int count = 0;
            while (!flag) {        // flag 为 false 就一直空转
                count++;
            }
            System.out.println("检测线程看到 flag=true，空转了 " + count + " 次，退出");
        }
    }
}
