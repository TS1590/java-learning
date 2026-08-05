public class Dog extends Animal {
    // 有参构造器：super() 调用父类构造器，必须是第一行
    public Dog(String name, int age) {
        super(name, age);
    }

    // 重写：方法签名与父类完全一致，加 @Override 注解（编译器帮你检查）
    @Override
    public void eat() {
        System.out.println(getName() + "在啃骨头");
    }

    // Dog 特有方法
    public void bark() {
        System.out.println(getName() + "：汪汪汪！");
    }
}
