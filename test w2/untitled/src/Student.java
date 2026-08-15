public class Student {
    private String name;
    private int score;
    private int age;

    public Student(String name, int score, int age) {
        this.name = name;
        this.score = score;
        this.age = age;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public int getAge() { return age; }

    @Override
    public String toString() {
        return name + "(" + score + "分," + age + "岁)";
    }
}
