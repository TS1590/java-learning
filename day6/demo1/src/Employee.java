public class Employee {
    private String name;
    private double salary;
    public Employee() {}
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    public String getName() {return name;}
    public double getSalary() {return salary;}
    public void setName(String name) {this.name = name;}
    public void setSalary(double salary) {this.salary = salary;}
    public void work(){
        System.out.println("员工在上班");
    }
}
