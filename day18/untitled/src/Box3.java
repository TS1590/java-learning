public class Box3 {
    public static void main(String[] args) {
        Shelf shelf = new Shelf();
        new Thread(new Producer(shelf), "生产者").start();
        new Thread(new Consumer(shelf), "消费者").start();
    }
}

// 货架：数组实现，最多放 3 件
class Shelf {
    private int[] goods = new int[3];   // 三个位置
    private int count = 0;              // 当前货量 0~3

    // 放货：满了就等
    public synchronized void put(int n) throws InterruptedException {
        while (count == 3) { wait(); }      // 满 → 等
        goods[count] = n;                   // 放第一个空位
        count++;
        System.out.println("生产了货物 " + n + "，货架上有 " + count + " 件");
        notifyAll();
    }

    // 取货：空了就等
    public synchronized int take() throws InterruptedException {
        while (count == 0) { wait(); }      // 空 → 等
        int n = goods[count - 1];           // 取最后一件
        count--;
        System.out.println("   消费了货物 " + n + "，货架剩 " + count + " 件");
        notifyAll();
        return n;
    }
}

class Producer1 implements Runnable {
    private Shelf shelf;
    public Producer1(Shelf shelf) { this.shelf = shelf; }
    public void run() {
        for (int i = 1; i <= 10; i++) {
            try { shelf.put(i); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}

class Consumer1 implements Runnable {
    private Shelf shelf;
    public Consumer1(Shelf shelf) { this.shelf = shelf; }
    public void run() {
        for (int i = 1; i <= 10; i++) {
            try { shelf.take(); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
