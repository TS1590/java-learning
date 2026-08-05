/**
 * Student 类：类 = 模板，对象 = 用模板造出来的具体实例
 */
public class Student {
    // 1. 成员变量（属性）：private 封装，外部不能直接访问
    private String name;    // 姓名
    private int age;        // 年龄
    private double score;   // 成绩

    // 2. 无参构造器：啥都不接收，先造一个"空对象"
    public Student() {
    }

    // 3. 有参构造器：一步创建完整对象
    //    重点：this.name = name 里的 this 指"当前这个对象"，
    //    左边 this.name 是成员变量，右边 name 是方法参数，名字撞了必须用 this 区分
    public Student(String name, int age, double score) {
        this.name = name;
        // 这里故意调用 setter 而不是直接 this.age = age，
        // 好处：校验逻辑只写一遍，构造器也自动享受校验
        this.setAge(age);
        this.setScore(score);
    }

    // 4. getter / setter：外部访问私有字段的唯一合法通道
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        // 范围校验：0-150 合法才赋值，非法就拒绝并提示
        if (age >= 0 && age <= 150) {
            this.age = age;
        } else {
            System.out.println("年龄不合法（0-150）：" + age);
        }
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        if (score >= 0 && score <= 100) {
            this.score = score;
        } else {
            System.out.println("成绩不合法（0-100）：" + score);
        }
    }

    // 5. 业务方法：打印学生信息
    public void printInfo() {
        System.out.println("我是" + name + "，今年" + age + "岁，成绩" + score);
    }
}
