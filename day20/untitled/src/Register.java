// RegisterFix.java —— Day 6 挑战练习修正版
// 练习要求：年龄<18 抛 AgeException（自定义）；密码长度<6 抛 IllegalArgumentException
// 修正点：①密码用 String + length() ②真正用上自定义异常 AgeException ③去掉复制粘贴残留

import java.util.Scanner;

public class Register {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("请输入年龄：");
        int age = sc.nextInt();
        sc.nextLine();                     // 吃掉数字后的换行符

        System.out.print("请输入密码：");
        String password = sc.nextLine();   // 密码是字符串，不是数字！

        // 自定义异常是受检异常：方法 throws 声明了，调用方必须 try-catch
        try {
            checkAge(age);
            System.out.println("年龄合法：" + age);
        } catch (AgeException e) {
            System.out.println("捕获异常：" + e.getMessage());
        }

        try {
            checkPw(password);
            System.out.println("密码合法：" + password);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常：" + e.getMessage());
        }

        System.out.println("注册流程结束");
    }

    // 自定义异常：AgeException extends Exception（受检），必须 throws 声明
    static void checkAge(int age) throws AgeException {
        if (age < 18) {
            throw new AgeException("未成年不能注册，年龄必须 ≥ 18，你输入了 " + age);
        }
    }

    // 密码长度用 String.length() 判断
    static void checkPw(String password) {
        if (password.length() < 6) {
            throw new IllegalArgumentException("密码长度至少 6 位，你输入了 " + password.length() + " 位");
        }
    }
}
