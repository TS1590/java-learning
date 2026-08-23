/**
 * W4 Day 5 · String 常量池 + == vs equals + StringBuilder
 *
 * 运行方式：
 *   javac StringPoolDemo.java
 *   java StringPoolDemo
 *
 * 观察点（对着输出看）：
 *  s1 == s2  → true  字面量相同内容 → 常量池里是同一个对象
 *  s1 == s3  → false new 强制在堆里新建，跟常量池不是同一个
 *  s1 == s4  → true  字面量拼接编译期就折叠成 "hello"
 *  s1 == s5  → false 变量拼接是运行期算的，产生新对象
 *  s1 == s6  → true  intern() 手动把堆里的字符串"送进"常量池
 */
public class StringPoolDemo {
    public static void main(String[] args) {
        // 1. 字面量：内容相同 → 常量池里复用同一个对象
        String s1 = "hello";
        String s2 = "hello";
        System.out.println("s1 == s2       → " + (s1 == s2));   // true

        // 2. new：强制在堆里新建对象，不参与常量池复用
        String s3 = new String("hello");
        System.out.println("s1 == s3       → " + (s1 == s3));   // false

        // 3. 字面量拼接：编译期就能确定 → 直接折叠成 "hello"
        String s4 = "hel" + "lo";
        System.out.println("s1 == s4       → " + (s1 == s4));   // true

        // 4. 变量拼接：编译期算不出来 → 运行期 new 新对象
        String part = "lo";
        String s5 = "hel" + part;
        System.out.println("s1 == s5       → " + (s1 == s5));   // false

        // 5. intern()：手动把内容相同的字符串"入池"，之后指向常量池那份
        String s6 = new String("world").intern();
        System.out.println("s1 == s6       → " + (s1 == s6));   // true

        // 6. 内容比较永远用 equals，别用 ==
        System.out.println("s3.equals(s1)  → " + s3.equals(s1)); // true
    }
}
