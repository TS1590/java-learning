import java.util.Arrays;

public class MergeSortedArray {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;       // nums1 有效区的末尾
        int j = n - 1;       // nums2 的末尾
        int k = m + n - 1;   // nums1 整个数组的末尾（填充位）

        while (j >= 0) {                    // nums2 没放完就一直放
            if (i >= 0 && nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];    // nums1 的大 → 放后面，i 左移
            } else {
                nums1[k--] = nums2[j--];    // 否则放 nums2 的，j 左移
            }
        }
        // 循环结束：nums2 已清空；nums1 剩余元素本来就在前面，天然有序，不用动
    }

    public static void main(String[] args) {
        MergeSortedArray sol = new MergeSortedArray();

        int[] t1 = {1, 2, 3, 0, 0, 0};
        sol.merge(t1, 3, new int[]{2, 5, 6}, 3);
        System.out.println("示例1 → 期望 [1,2,2,3,5,6]，实际 " + Arrays.toString(t1));

        int[] t2 = {1};
        sol.merge(t2, 1, new int[]{}, 0);
        System.out.println("示例2 → 期望 [1]，实际 " + Arrays.toString(t2));

        int[] t3 = {0};
        sol.merge(t3, 0, new int[]{1}, 1);
        System.out.println("示例3 → 期望 [1]，实际 " + Arrays.toString(t3));
    }
}
