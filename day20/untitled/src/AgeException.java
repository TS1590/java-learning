// AgeException.java —— 自定义异常（校验年龄）
// extends Exception = 受检异常（编译期强制处理）

public class AgeException extends Exception {
    public AgeException(String message) {
        super(message);   // 把错误信息传给 Exception 父类
    }
}
