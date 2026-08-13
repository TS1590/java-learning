import java.util.*;

public class GenericDemo {
    public static void main(String[] args) {
        // ========== 泛型类：Box<T> 装任意类型 ==========
        Box<String> box1 = new Box<>();
        box1.set("Java");
        String str = box1.get();          // 取出直接是 String，不用强转！
        System.out.println("字符串：" + str);

        Box<Integer> box2 = new Box<>();
        box2.set(100);
        Integer num = box2.get();          // 取出直接是 Integer
        System.out.println("数字：" + num);

        // ========== 泛型方法 ==========
        Integer[] intArr = {1, 2, 3};
        String[] strArr = {"a", "b", "c"};
        printArray(intArr);                // 传 Integer[]
        printArray(strArr);                // 传 String[]，同一个方法都能接
    }

    // 泛型方法：<T> 写在返回值前面，参数和返回都能用 T
    public static <T> void printArray(T[] arr) {
        System.out.println(Arrays.toString(arr));
    }
}

// 泛型类：类名后面 <T>，T 是"类型占位符"
class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
