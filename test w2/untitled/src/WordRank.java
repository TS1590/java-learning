import java.util.*;

public class WordRank {
    public static void main(String[] args) {
        String[] words = {"java","python","java","go","java","python","c++","go","java"};

        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            map.put(words[i], map.getOrDefault(words[i], 0) + 1);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() != o1.getValue()
                        ? o2.getValue() - o1.getValue()   // 先比次数降序
                        : o1.getKey().compareTo(o2.getKey());  // 次数相同比字母升序

            }
        });

        for (int i = 0; i < Math.min(3, list.size()); i++) {
            System.out.println(list.get(i).getKey() + ": " + list.get(i).getValue() + " 次");
        }
    }
}
