import java.util.HashMap;

public class ScoreAvg2 {
    public static void main(String[] args) {
        HashMap<String, Integer> score = new HashMap<>();

        score.put("小明",90);
        score.put("小红",85);
        score.put("小刚",94);

        int sum = 0;
        int count = 0;
        for (String key : score.keySet()) {
            System.out.println(key + "的成绩是" + score.get(key));
            sum += score.get(key);
            count++;
        }

        System.out.println("平均分是" + (double)sum);
    }
}
