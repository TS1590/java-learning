public class AnimalTest {
    public static void main(String[] args) {
        // 多态：父类引用 指向 子类对象（向上转型）
        Animal a1 = new Dog("大黄", 2);
        Animal a2 = new Cat("咪咪", 1);

        // 同一句 a.eat()，两个对象表现不同 —— 这就是多态
        a1.eat();  // 大黄在啃骨头（执行的是 Dog 重写后的版本）
        a2.eat();  // 咪咪在吃鱼（执行的是 Cat 重写后的版本）

        // 注意：Animal 类型的引用只能调用父类里有的方法
        // a1.bark();  // ❌ 编译报错：Animal 类型里没有 bark()

        // 想调用子类特有方法 → 向下转型，先 instanceof 判断类型
        if (a1 instanceof Dog) {
            Dog d = (Dog) a1;
            d.bark();  // 大黄：汪汪汪！
        }
    }
}
