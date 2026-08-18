public class ThreadSleep {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("倒计时开始：");
        for (int i = 3; i >= 1; i--) {
            System.out.println(i + "...");
            Thread.sleep(1000);   // 睡 1 秒（1000 毫秒）——注意要处理异常
        }
        System.out.println("时间到！");
    }
}
