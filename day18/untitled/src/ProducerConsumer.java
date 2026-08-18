// ProducerConsumer.java —— Day 4 生产者-消费者（线程通信 wait/notify）
// 场景：一个只能放 1 件货的"货架"，生产者放、消费者拿，交替进行

public class ProducerConsumer {
    public static void main(String[] args) {
        Box box = new Box();                       // 共享货架
        new Thread(new Producer(box), "生产者").start();
        new Thread(new Consumer(box), "消费者").start();
    }
}

// 货架：只能放 1 件货
class Box {
    private int goods;             // 货物编号
    private boolean hasGoods = false;  // 货架上有没有货

    // 放货（生产者调用）
    public synchronized void put(int n) throws InterruptedException {
        while (hasGoods) {          // 有货 → 等着，等消费者拿走
            wait();                 // 释放锁，进入等待；被唤醒后回来重新判断
        }
        goods = n;
        hasGoods = true;
        System.out.println("生产了货物 " + n);
        notify();                   // 唤醒一个等待中的线程（消费者）
    }

    // 取货（消费者调用）
    public synchronized int take() throws InterruptedException {
        while (!hasGoods) {         // 没货 → 等着，等生产者放货
            wait();
        }
        System.out.println("   消费了货物 " + goods);
        hasGoods = false;
        notify();                   // 唤醒生产者
        return goods;
    }
}

class Producer implements Runnable {
    private Box box;

    public Producer(Box box) { this.box = box; }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            try {
                box.put(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable {
    private Box box;

    public Consumer(Box box) { this.box = box; }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            try {
                box.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
