import java.util.Random;
import java.util.Scanner;

public class GuessNumber {
    public static void main(String[] args) {
        // 1. 系统随机生成 1-100 的答案
        Random random = new Random();
        int target = random.nextInt(100) + 1;  // nextInt(100) 生成 0~99,+1 变成 1~100

        // 2. 准备键盘输入
        Scanner scanner = new Scanner(System.in);

        int guess;     // 用户猜的数
        int count = 0; // 统计猜了多少次

        System.out.println("我已经想好了一个 1-100 之间的数字,快来猜吧!");

        // 3. 循环:只要没猜中就一直猜
        do {
            System.out.print("请输入你的猜测:");
            guess = scanner.nextInt();
            count++;

            if (guess > target) {
                System.out.println("猜大了,再小一点~");
            } else if (guess < target) {
                System.out.println("猜小了,再大一点~");
            } else {
                System.out.println("恭喜你,猜中了!答案是 " + target
                        + ",你一共猜了 " + count + " 次。");
            }
        } while (guess != target);   // 条件在末尾:先玩一次,再判断要不要继续

        scanner.close();
    }
}
