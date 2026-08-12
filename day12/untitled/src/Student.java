public class Student implements Comparable<Student> {
    private String name;
    private int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public int getScore() { return score; }

    @Override
    public String toString() {
        return name + ":" + score;
    }

    // 自然排序：默认按分数升序
    @Override
    public int compareTo(Student o) {
        return this.score - o.score;
    }
}
