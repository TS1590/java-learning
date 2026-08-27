/**
 * W4 Day 7 · 手写栈（Stack）+ 队列思路
 *
 * 栈（Stack）= 后进先出 LIFO：像叠盘子/弹夹，后放的先拿
 *   常用操作：push 入栈、pop 出栈（取栈顶并移除）、peek 看栈顶（不移除）
 * 队列（Queue）= 先进先出 FIFO：像排队，先来的先走
 *   常用操作：offer 入队、poll 出队、peek 看队头
 *
 * 观察点：main 里 push 1、2、3，pop 出来却是 3、2 —— 后进先出
 */
public class MyStack {

    private int[] arr;
    private int top;      // 栈顶下标：-1 表示空栈

    public MyStack(int capacity) {
        arr = new int[capacity];
        top = -1;
    }

    // 入栈：放到栈顶，top 上移一格
    public void push(int value) {
        if (top == arr.length - 1) {
            throw new RuntimeException("栈满了");
        }
        arr[++top] = value;      // 先 top+1，再放值
    }

    // 出栈：取栈顶，top 下移一格
    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("栈是空的");
        }
        return arr[top--];       // 返回值，再 top-1
    }

    // 看栈顶（不弹出）
    public int peek() {
        if (isEmpty()) {
            throw new RuntimeException("栈是空的");
        }
        return arr[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }

    public static void main(String[] args) {
        MyStack stack = new MyStack(5);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("栈顶 peek = " + stack.peek());   // 3（最后一个放进去的）
        System.out.println("pop = " + stack.pop());          // 3 ← 后进先出！
        System.out.println("pop = " + stack.pop());          // 2
        System.out.println("size = " + stack.size());        // 1
        System.out.println("isEmpty = " + stack.isEmpty());  // false
    }
}
