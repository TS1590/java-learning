/**
 * W4 Day 6 · 手写单链表（数据结构核心）
 *
 * 链表 vs 数组的本质区别：
 *   数组 = 连续内存，按下标直接跳（O(1)），但插入删除要挪元素（O(n)）
 *   链表 = 节点串联，每个节点只记住"下一个是谁"，插入删除只改指针（O(1)），
 *          但按下标找要一个一个走（O(n)）
 *
 * 观察点：跟着 main 里的输出看 add/get/addFirst/remove 各自做了什么
 */
public class MyLinkedList {

    // 节点内部类：链表的最小单元 = 一个值 + 指向下一个的指针
    static class Node {
        int value;
        Node next;              // 关键！记住"下一个节点"的地址

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;   // 头节点：链表的入口（null = 空链表）
    private int size;    // 元素个数

    // 尾部添加（新节点要"接"到最后一个节点的后面）
    public void add(int value) {
        Node newNode = new Node(value);
        if (head == null) {          // 空链表：新节点直接当 head
            head = newNode;
        } else {
            Node cur = head;
            while (cur.next != null) {   // 走到最后一个节点
                cur = cur.next;
            }
            cur.next = newNode;      // 让最后一个节点指向新节点
        }
        size++;
    }

    // 指定下标插入：核心 = 先找到"前一个节点"(prev)，再改两个指针
    public void addPro(int index, int value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("下标越界: " + index);
        }
        Node newNode = new Node(value);

        if (index == 0) {            // 特殊：头插，直接复用 addFirst 逻辑
            newNode.next = head;
            head = newNode;
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {   // 走到 index 的【前一个】节点
                prev = prev.next;
            }
            newNode.next = prev.next;   // ① 新节点先接住"后面那段"（顺序不能反！）
            prev.next = newNode;        // ② 前一个节点再接住新节点
        }
        size++;
    }


    // 头部插入：链表的强项，O(1)，只要改一个指针
    public void addFirst(int value) {
        Node newNode = new Node(value);
        newNode.next = head;   // 新节点指向旧头
        head = newNode;        // 新节点成为新头
        size++;
    }

    // 按下标取值：链表的痛点，必须从头一个一个走，O(n)
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("下标越界: " + index);
        }
        Node cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;        // 一步步往下走
        }
        return cur.value;
    }

    // 按值删除第一个匹配：核心操作 = 改指针，跳过要删的节点
    public boolean remove(int value) {
        if (head == null) {
            return false;
        }
        if (head.value == value) {     // 要删的是头节点
            head = head.next;          // 头指针直接跳过
            size--;
            return true;
        }
        Node cur = head;
        while (cur.next != null) {
            if (cur.next.value == value) {   // 找到"要删节点的前一个"
                cur.next = cur.next.next;    // 让前一个直接指向"要删节点的下一个"
                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;                  // 没找到
    }

    public int size() {
        return size;
    }

    // 打印整条链：1 -> 2 -> 3 -> null
    public void print() {
        Node cur = head;
        while (cur != null) {
            System.out.print(cur.value + " -> ");
            cur = cur.next;
        }
        System.out.println("null");
    }

    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.addPro(1,99);
        list.print();                    // 1 -> 2 -> 3 -> null
        list.addFirst(0);
        list.print();                    // 0 -> 1 -> 2 -> 3 -> null
        System.out.println("get(2) = " + list.get(2));   // 2（从头走 2 步）
        list.remove(2);
        list.print();                    // 0 -> 1 -> 3 -> null
        System.out.println("size = " + list.size());     // 3
    }
}
