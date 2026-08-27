import java.util.Stack;

/**
 * W4 Day 7 · 相似练习：删除字符串中的所有相邻重复项（LeetCode 1047）
 *
 * 思路：和有效括号同一个套路 —— 用栈做"栈顶比对"
 *   当前字符 和 栈顶相同 → 弹出（消除这一对相邻重复）
 *   当前字符 和 栈顶不同 → 入栈
 *   最后栈里从底到顶拼起来就是答案
 *
 * 验证：abbaca -> ca / azxxzy -> ay / aaaa -> "" / abba -> ""
 */
public class RemoveDuplicates1047 {

    public static String removeDuplicates(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (!stack.isEmpty() && stack.peek() == c) {
                stack.pop();        // 和栈顶相同 → 相邻重复，消除
            } else {
                stack.push(c);      // 不同 → 入栈等着
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c : stack) {      // 栈底 → 栈顶，保持原顺序
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(removeDuplicates("abbaca")); // ca
        System.out.println(removeDuplicates("azxxzy")); // ay
        System.out.println(removeDuplicates("aaaa"));   // (空)
        System.out.println(removeDuplicates("abba"));   // (空) 连环消除
        System.out.println(removeDuplicates("a"));      // a
    }
}
