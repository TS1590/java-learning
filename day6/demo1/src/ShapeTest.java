public class ShapeTest {
    public static void main(String[] args) {
        // 第1步：创建长度 3 的 Shape 数组
        Shape[] shapes = new Shape[3];

        // 第2步：填入 2 个圆 + 1 个矩形（多态，这两行半你来写）
        shapes[0] = new Circle(1);
        shapes[1] = new Circle(2);
        shapes[2] = new Rectangle(3, 4);

        // 第3步：循环累加（这段不用改）
        double total = 0;
        for (int i = 0; i < shapes.length; i++) {
            total += shapes[i].area();
        }
        System.out.println("总面积 = " + total);
    }
}
