public class Cat extends Animal {
    public Cat(String name, int age) {
        super(name, age);
    }

    @Override
    public void eat() {
        System.out.println(getName() + "在吃鱼");
    }

    // Cat 特有方法
    public void meow() {
        System.out.println(getName() + "：喵喵喵！");
    }
}
