public class MySort {
    public static void main(String[] args) {
        int[] arr = {9, 4, 7, 2, 6};

        // 外层循环：需要 n-1 轮（5 个数，冒 4 轮就排好了）
        for (int i = 0; i < arr.length - 1; i++) {
            // 内层循环：每轮比较相邻两个，把大的往后换
            // 注意 j 的上限：每轮少比较一个（末尾已排好的不用管）
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换两个数
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        int min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {        // 发现更大的，替换
                min = arr[i];
            }
        }
        // 打印结果
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println("最小值是：" + min);
    }
}
