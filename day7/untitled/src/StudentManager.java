import java.util.Scanner;

public class StudentManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Student[] students = new Student[100]; // 数组存学生
        int size = 0;   // ★ 记录当前有几个学生（重要！）

        while (true) {
            System.out.println("1.录入 2.显示 3.排序 4.查找 5.修改 6.删除 0.退出");
            int choice = sc.nextInt();
            switch (choice) {
                case 1: // 录入
                    System.out.print("姓名：");
                    String name = sc.next();
                    System.out.print("成绩：");
                    int score = sc.nextInt();
                    students[size] = new Student(name, score);
                    size++;   // ★ 别忘了！
                    break;
                case 2: // 显示
                    for (int i = 0; i < size; i++) {
                        System.out.println(students[i].getName() + " " + students[i].getScore());
                    }
                    break;
                case 3:
                    for (int i = 0; i < size - 1; i++) {
                        for (int j = 0; j < size - 1 - i; j++) {
                            if (students[j].getScore() < students[j + 1].getScore()) {  // 降序：小的往后换
                                Student temp = students[j];
                                students[j] = students[j + 1];
                                students[j + 1] = temp;
                            }
                        }
                    }
                    for (int i = 0; i < size; i++) {
                        System.out.println(students[i].getName() + " " + students[i].getScore());
                    }
                    break;
                case 4:
                    System.out.print("姓名：");
                    String name1 = sc.next();
                    int index = findStudent(students, size, name1);
                    if (index == -1) {
                        System.out.println("未找到此人");
                    } else {
                        System.out.println(students[index].getName() + " " + students[index].getScore());
                    }
                    break;
                case 5:
                    System.out.print("需要修改成绩的姓名：");
                    String name2 = sc.next();
                    System.out.print("新的成绩：");
                    int score1 = sc.nextInt();
                    int index1 = findStudent(students, size, name2);
                    if (index1 == -1) {
                        System.out.println("未找到此人");
                    } else {
                        students[index1].setScore(score1);
                    }
                    break;
                case 6:
                    System.out.print("需要删除的姓名：");
                    String name3 = sc.next();
                    int index2 = findStudent(students, size, name3);
                    if (index2 == -1) {
                        System.out.println("未找到此人");
                    } else {
                        for (int i = index2; i < size - 1; i++) {
                            students[i] = students[i + 1];   // index 后面的所有元素往前挪
                        }
                        size--;
                    }
                    break;
                case 0:
                    System.out.println("再见");
                    return;  // ★ 退出用 return，不是 break！
            }
        }

    }

    public static int findStudent(Student[] students, int size, String name) {
        for (int i = 0; i < size; i++) {
            if (students[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}

