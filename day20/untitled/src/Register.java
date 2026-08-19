import java.util.*;

public class Register {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入年龄：");
        int age = sc.nextInt();
        Scanner pw = new Scanner(System.in);
        System.out.print("请输入密码：");
        int password = pw.nextInt();

        try {
            checkAge(age);          // 可能抛异常
            System.out.println("年龄合法：" + age);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常：" + e.getMessage());
        } finally {
            System.out.println("finally 总会执行（关资源放这里）");
        }

        try {
            checkPw(password);          // 可能抛异常
            System.out.println("密码合法：" + password);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常：" + e.getMessage());
        } finally {
            System.out.println("finally 总会执行（关资源放这里）");
        }
    }

    static void checkAge(int age) {
        if (age < 0 || age > 18) {
            throw new IllegalArgumentException("年龄必须在 0~150 之间，你输入了 " + age);  // throw：主动抛出
        }else if (age < 18){
            throw new IllegalArgumentException("未成年不能注册");
        }
    }

    static void checkPw(int password) {
        if (password < 0 || password > 99999) {
            throw new IllegalArgumentException("密码长度要小于6位数，你输入了 " + password);  // throw：主动抛出
        }
    }
}
