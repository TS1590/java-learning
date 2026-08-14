import java.util.HashMap;

public class SingleNumber {
    public int singleNumber(int[] nums) {          // 返回 int，不是 int[]！
        HashMap<Integer, Integer> map = new HashMap<>();

        // 第 1 遍：数次数
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i],0) + 1);   // 填空①：用 getOrDefault，上次次数+1
        }

        // 第 2 遍：找次数为 1 的
        for (Integer key : map.keySet()) {
            if (map.get(key) == 1) {                 // 填空②：判断这个 key 的次数是不是 1
                return key;
            }
        }
        return -1;   // 题目保证有答案，走不到这（返回 -1 表示没找到）
    }

    public static void main(String[] args) {
        SingleNumber sn = new SingleNumber();

        int[] test1 = {4, 1, 2, 1, 2};
        System.out.println("测试1 [4,1,2,1,2] → 期望 4，实际 " + sn.singleNumber(test1));

        int[] test2 = {2, 2, 1};
        System.out.println("测试2 [2,2,1] → 期望 1，实际 " + sn.singleNumber(test2));

        int[] test3 = {1};    // 边界：只有一个元素
        System.out.println("测试3 [1] → 期望 1，实际 " + sn.singleNumber(test3));
    }
}
