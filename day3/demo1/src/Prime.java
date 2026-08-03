public class Prime {
    public static void main(String[] args) {
        // 外层循环:逐个检查 2~100
        for (int i = 2; i <= 100; i++) {
            boolean isPrime = true;   // 先假设 i 是质数
            // 内层循环:从 2 试除到 i-1
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {     // 能被整除 → 不是质数
                    isPrime = false;
                    break;            // 已经确定不是了,不用再试
                }
            }
            if (isPrime) {
                System.out.print(i + " ");
            }
        }
    }
}
