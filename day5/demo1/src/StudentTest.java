/**
 * StudentTest：测试类，main 是程序入口，负责创建对象
 */
public class StudentTest {
    public static void main(String[] args) {
        // 对象1：无参构造器创建，再用 setter 逐个设置
        Student s1 = new Student();
        s1.setName("小明");
        s1.setAge(18);
        s1.setScore(92.5);
        s1.printInfo();

        // 对象2：有参构造器一步到位
        Student s2 = new Student("小红", 19, 88.0);
        s2.printInfo();

        // 对象3：故意把 age 设成 200，看 setter 是否拦截
        Student s3 = new Student();
        s3.setAge(200);          // 应该被拦截
        s3.setName("小刚");
        s3.setScore(75.5);
        s3.printInfo();          // age 没被改，还是默认值 0
    }
}
