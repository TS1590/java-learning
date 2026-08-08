public class pass {
    // 方法2：传入 int[]，返回及格人数（>=60 算及格）
    public static void main(String[] args) {
        int[] arr = {50,60,70,80};
        int count = 0;
        for(int i=0;i<arr.length;i++){
            if(arr[i] >= 60){
                count++;
            }
        }
        System.out.println(count);
    }
}
