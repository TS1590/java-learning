import java.util.*;

public class StudentTest {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        list.add(new Student("张三", 70));
        list.add(new Student("李四", 90));
        list.add(new Student("王五", 60));

        // 方式1：默认规则（Student 实现了 Comparable，按分数升序）
        Collections.sort(list);
        System.out.println("Comparable 升序：" + list);

        // 方式2：临时换规则 —— 按姓名排序（不用改 Student 类）
        Collections.sort(list, new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return a.getName().compareTo(b.getName());
            }
        });
        System.out.println("Comparator 按姓名：" + list);

        // 方式3：按分数降序（反着减 = 降序）
        Collections.sort(list, new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return b.getScore() - a.getScore();
            }
        });
        System.out.println("Comparator 降序：" + list);

        // 方式4：Lambda 简写（和方式3完全等价，先眼熟，后面专门学）
        Collections.sort(list, (a, b) -> b.getScore() - a.getScore());
        System.out.println("Lambda 降序：" + list);
    }
}
