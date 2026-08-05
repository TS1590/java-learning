public class BankAccount {
    private String owner;    // 户主
    private double balance;  // 余额

    // 有参构造器：户主 + 初始金额（金额也要校验 >= 0）
    public BankAccount(String owner, double initialMoney) {
        this.owner = owner;
        if (initialMoney >= 0) {
            this.balance = initialMoney;
        }
    }

    // getBalance：只读，不提供 setBalance（这是本练习的核心考点）
    public double getBalance() {
        return balance;
    }

    // 示范：deposit 存钱
    public void deposit(double money) {
        if (money <= 0) {
            System.out.println("存款金额必须大于 0");
        } else {
            this.balance += money;   // 余额累加
            System.out.println("存入 " + money + " 元，当前余额 " + balance);
        }
    }

    // ✏️ 你来写：withdraw 取钱
    // 提示：比 deposit 多一个条件——money > balance 也不行（余额不足）
    // 两个条件用 || 连起来：if (money <= 0 || money > balance) { 拒绝 } else { balance -= money }
    public void withdraw(double money) {
        if (money <= 0) {
            System.out.println("取款金额必须大于 0");
        } else if (this.balance < money) {
            System.out.println("余额不足");
        }else {
            this.balance -= money;
            System.out.println("取出 " + money + " 元，当前余额 " + balance);
        }
    }

    public void showInfo() {
        System.out.println(owner + " 的余额是 " + balance + " 元");
    }
}
