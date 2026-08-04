public class ArrayMaxAvg {
    public static void main(String[] args) {
        int[] arr = {3, 7, 2, 9, 5};

        // 求最大值：假设第一个最大，遍历比较
        int max = arr[0];              // 先假设 arr[0] 是最大值
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {        // 发现更大的，替换
                max = arr[i];
            }
            sum += arr[i];             // 顺便累加求和
        }
        double avg = (double) sum / arr.length;   // 注意：转 double，否则除法会丢小数

        System.out.println("最大值：" + max);
        System.out.println("平均值：" + avg);
    }
}
