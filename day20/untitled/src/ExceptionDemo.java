// ExceptionDemo.java —— Day 6 异常处理
// 三种处理：try-catch-finally / throws 声明 / throw 抛出

import java.util.Scanner;

public class ExceptionDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入年龄：");
        int age = sc.nextInt();

        try {
            checkAge(age);          // 可能抛异常
            System.out.println("年龄合法：" + age);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常：" + e.getMessage());
        } finally {
            System.out.println("finally 总会执行（关资源放这里）");
        }
        System.out.println("程序继续往下走");
    }

    // throws：声明这个方法可能抛异常，交给调用方处理
    static void checkAge(int age) {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("年龄必须在 0~150 之间，你输入了 " + age);  // throw：主动抛出
        }
    }
}
