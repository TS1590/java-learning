public class BankAccountTest {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount("张三", 100);  // 初始 100
        acc.showInfo();      // 张三 的余额是 100.0 元

        acc.deposit(100);    // 存入 100 → 余额 200
        acc.withdraw(30);    // 取 30   → 余额 170
        acc.withdraw(200);   // 余额不足，应该被拒绝 → 还是 170
        acc.deposit(-5);     // 非法，应该被拒绝
        acc.showInfo();
    }
}
