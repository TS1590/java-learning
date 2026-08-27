import java.util.Stack;

/**
 * W4 Day 7 · 挑战题：有效的括号（LeetCode 20，面试高频）
 *
 * 思路：栈的经典应用 —— "最近的期待"
 *   遇到左括号 ( [ { → 入栈（期待一个右括号来配它）
 *   遇到右括号 ) ] } → 从栈顶弹出最近的左括号，看配不配
 *   不配 / 栈空（没有左括号可配）→ false
 *   全部扫完，栈必须为空（不能有没人配的左括号）→ true
 *
 * 验证：() true / ()[]{} true / (] false / ([)] false / {[]} true
 */
public class ValidParentheses {

    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);            // 左括号：先存着，等它的右括号
            } else {
                if (stack.isEmpty()) {
                    return false;         // 来了右括号但栈空 → 没左括号可配
                }
                char left = stack.pop();  // 弹出最近的一个左括号
                if (c == ')' && left != '(') return false;
                if (c == ']' && left != '[') return false;
                if (c == '}' && left != '{') return false;
            }
        }
        return stack.isEmpty();           // 全配对了 → 栈空 → true
    }

    public static void main(String[] args) {
        System.out.println(isValid("()"));     // true
        System.out.println(isValid("()[]{}")); // true
        System.out.println(isValid("(]"));     // false
        System.out.println(isValid("([)]"));   // false
        System.out.println(isValid("{[]}"));   // true
    }
}
