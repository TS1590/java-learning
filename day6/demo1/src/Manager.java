public class Manager extends Employee {
    public Manager(String name, double salary) {
        super(name, salary);
    }
    @Override
    public void work() {
        System.out.println("经理在开会");
    }
    public void bonus() {
        System.out.println("经理发奖金");
    }
}
