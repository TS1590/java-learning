import java.util.Scanner;   // 第 1 行：导入

public class ScannerDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);   // 创建扫描器
        System.out.print("请输入你的年龄：");
        int age = sc.nextInt();                // 读取整数
        System.out.println("你输入了：" + age);
        sc.close();                            // 用完关闭（好习惯）
    }
}
