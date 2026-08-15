import java.util.*;

public class StudentRank {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        list.add(new Student("张三", 85, 20));
        list.add(new Student("李四", 92, 21));
        list.add(new Student("王五", 92, 19));   // 和李四同分，年龄更小
        list.add(new Student("赵六", 58, 22));   // 不及格
        list.add(new Student("孙七", 70, 20));

        // 1. 排序：成绩降序，同分按年龄升序（Comparator 链式）
        list.sort(Comparator.comparingInt(Student::getScore)  // 先按成绩
                .reversed()                                   // 降序
                .thenComparingInt(Student::getAge));          // 同分按年龄升序

        System.out.println("排序后：");
        for (Student s : list) {
            System.out.println(s);
        }

        // 2. Iterator 安全删除不及格的
        Iterator<Student> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().getScore() < 60) {
                it.remove();
            }
        }

        System.out.println("删除不及格后：");
        for (Student s : list) {
            System.out.println(s);
        }
    }
}
