public class EmployeeTest {
    public static void main(String[] args) {
        Employee e1 = new Manager("John", 10000);
        e1.work();
        if (e1 instanceof Manager) {
            Manager m = (Manager) e1;
            m.bonus();
        }
    }
}
