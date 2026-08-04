public class test {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        int left = 0;                    // 左指针，从最左边开始
        int right = arr.length - 1;      // 右指针，从最右边开始
        while (left < right) {           // 没相遇就继续换
            int temp = arr[left];        // 交换 arr[left] 和 arr[right]
            arr[left] = arr[right];
            arr[right] = temp;
            left++;                      // 左边往右走一步
            right--;                     // 右边往左走一步
        }
        for (int num : arr) {
            System.out.print(num + " "); // 输出 5 4 3 2 1
        }
    }
}
