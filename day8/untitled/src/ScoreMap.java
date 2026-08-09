import java.util.HashMap;

public class ScoreMap {
    public static void main(String[] args) {
        HashMap<String, Integer> score = new HashMap<>();
        score.put("小明", 90);
        score.put("小红", 85);
        score.put("小刚", 95);

        for (String key : score.keySet()) {
            System.out.println(key + " " +score.get(key));
        }

        int sum = 0;
        int num = 0;
        for (String key : score.keySet()) {
            sum += score.get(key);
            num++;
        }
        System.out.println(sum/num);
    }
}
