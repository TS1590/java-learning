/**
 * W4 Day 4 · 类加载机制 + 类加载器 + 双亲委派 演示
 *
 * 运行方式：
 *   javac ClassLoaderDemo.java
 *   java ClassLoaderDemo
 *
 * 观察点：
 *  1. Object/String 的加载器打印 null → 它们是"启动类加载器"加载的（C++ 写的，Java 看不到）
 *  2. 自己写的类加载器是 AppClassLoader（应用类加载器）
 *  3. getParent() 一路往上 → 形成"父子链条"（这就是双亲委派的载体）
 */
public class ClassLoaderDemo {
    public static void main(String[] args) throws Exception {
        // 1. 三个类分别由谁加载？
        System.out.println("Object 的加载器：" + Object.class.getClassLoader());
        System.out.println("String 的加载器：" + String.class.getClassLoader());
        System.out.println("我自己类的加载器：" + ClassLoaderDemo.class.getClassLoader());
        System.out.println();

        // 2. 从自己开始，一路打印加载器链条（getParent 往上找爸爸）
        ClassLoader cl = ClassLoaderDemo.class.getClassLoader();
        while (cl != null) {
            System.out.println("  加载器：" + cl);
            cl = cl.getParent();
        }
        System.out.println("  再往上 = 启动类加载器 Bootstrap（C++ 写的，打印 null）");
        System.out.println();

        // 3. Class.forName 也会触发类的加载（反射常用）
        Class<?> c = Class.forName("java.util.ArrayList");
        System.out.println("forName 加载 ArrayList 成功：" + c.getName());
    }
}
