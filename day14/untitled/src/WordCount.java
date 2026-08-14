import java.util.*;

public class WordCount {
    public static void main(String[] args) {
        // 一段英文文本（单词间用空格隔开）
        String text = "java is fun java is powerful java is the best";

        // 1. 按空格拆成单词数组
        String[] words = text.split(" ");

        // 2. HashMap 统计每个单词出现的次数（getOrDefault 一步到位）
        Map<String, Integer> map = new HashMap<>();
        for (String w : words) {
            map.put(w, map.getOrDefault(w, 0) + 1);
        }

        // 3. 转成 List，按次数降序排序（Comparator 反着减 = 降序）
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });

        // 4. 遍历输出
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
