public class Day1Types {
    public static void main(String[] args) {
        // 8 种基本类型，每种声明一个变量并打印
        byte b = 100;
        short s = 10000;
        int i = 1000000;
        long l = 10000000000L;   // long 类型数值要加 L
        float f = 3.14F;         // float 类型数值要加 F
        double d = 3.1415926;
        char c = 'A';            // 单引号包一个字符
        boolean flag = true;     // 只有 true / false

        System.out.println("byte: " + b);
        System.out.println("short: " + s);
        System.out.println("int: " + i);
        System.out.println("long: " + l);
        System.out.println("float: " + f);
        System.out.println("double: " + d);
        System.out.println("char: " + c);
        System.out.println("boolean: " + flag);
    }
}
