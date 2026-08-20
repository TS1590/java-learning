public class Singleton {
    // volatile：防止"半初始化对象"被其他线程看到（关键！）
    private static volatile Singleton instance;

    // 私有构造器：外面 new 不出来，只能走 getInstance()
    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {                  // ① 外层检查：没锁，快
            synchronized (Singleton.class) {     // ② 锁类对象（static 锁的就是这个）
                if (instance == null) {          // ③ 内层检查：防重复创建
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
