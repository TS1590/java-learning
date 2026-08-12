import java.util.*;

public class ToolsDemo {
    public static void main(String[] args) {
        // ========== Collections 工具类（操作集合） ==========
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(2);
        list.add(8);
        list.add(1);
        list.add(9);

        System.out.println("原始：" + list);

        Collections.sort(list);              // 升序排序
        System.out.println("sort 升序：" + list);

        Collections.reverse(list);           // 反转
        System.out.println("reverse 反转：" + list);

        Collections.shuffle(list);           // 随机打乱
        System.out.println("shuffle 打乱：" + list);

        System.out.println("max：" + Collections.max(list));
        System.out.println("min：" + Collections.min(list));

        Collections.fill(list, 0);           // 全部填充 0
        System.out.println("fill 填充0：" + list);

        // ========== Arrays 工具类（操作数组） ==========
        int[] arr = {5, 2, 8, 1, 9};
        Arrays.sort(arr);                    // 数组排序
        System.out.println("数组排序：" + Arrays.toString(arr));

        int[] copy = Arrays.copyOf(arr, 7);  // 扩容拷贝（新长度7，多出的补0）
        System.out.println("copyOf：" + Arrays.toString(copy));

        int idx = Arrays.binarySearch(arr, 8); // 二分查找（前提：数组已有序）
        System.out.println("8 的下标：" + idx);
    }
}
