/**
 * LeetCode 206 · 反转链表（三指针迭代法）
 *
 * 口诀：先存 next → 掉头 → prev 追 → curr 追
 * 1. next = curr.next   先保存下一个，防止掉头后断链
 * 2. curr.next = prev   当前节点掉头，指向前一个
 * 3. prev = curr        prev 前移一位
 * 4. curr = next        curr 前移一位
 *
 * 时间复杂度 O(n)，空间复杂度 O(1)
 */
public class ReverseList {

    // 链表节点（LeetCode 环境里已内置，本地自己写一份）
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
        }
    }

    static class Solution {
        public ListNode reverseList(ListNode head) {
            ListNode prev = null; // 已反转部分的头（初始 null）
            ListNode curr = head; // 当前要处理的节点
            while (curr != null) {
                ListNode next = curr.next; // ① 先存住后面的，防止断链
                curr.next = prev;          // ② 掉头：当前节点指向前一个
                prev = curr;               // ③ prev 前移
                curr = next;               // ④ curr 前移
            }
            return prev; // 结束时 prev 就是新链表的头
        }
    }

    public static void main(String[] args) {
        // 构造链表：1 -> 2 -> 3 -> null
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);

        ListNode newHead = new Solution().reverseList(head);

        // 打印反转结果：3 2 1
        while (newHead != null) {
            System.out.print(newHead.val + " ");
            newHead = newHead.next;
        }
    }
}
