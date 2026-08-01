import java.util.Scanner;   // 第 1 行：导入

public class Calculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);   // 创建扫描器
        System.out.print("请输入第一个数：");
        double first = sc.nextDouble();
        System.out.print("请输入第二个数：");
        double second = sc.nextDouble();     // 读取整数
        System.out.println(first + second);
        System.out.println(first - second);
        System.out.println(first * second);
        System.out.println(first / second);
        System.out.println(first % second);
        sc.close();                            // 用完关闭（好习惯）
    }
}

