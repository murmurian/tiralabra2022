import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter an expression or quit with q: ");
            String expression = scanner.nextLine().replaceAll("\\s", "");
            if (expression.equals("q"))
                break;
            Queue<String> postfix = infixToPostfix(expression);
            System.out.println("The result is: " + evaluate(postfix));
        }
    }

    /**
     * Converts an infix expression to a postfix expression
     * @param expression the infix expression
     * @return the postfix as a queue expression
     */
    private static Queue<String> infixToPostfix(String expression) {
        Queue<String> queue = new ArrayDeque<>();
        Stack<String> stack = new Stack<>();

        String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])");

        for (String token : tokens) {
            switch (token) {
                case "(" -> stack.push(token);
                case ")" -> {
                    while (!stack.peek().equals("("))
                        queue.add(stack.pop());
                    stack.pop();
                }
                case "+", "-" -> {
                    while (!stack.isEmpty() && !stack.peek().equals("("))
                        queue.add(stack.pop());
                    stack.push(token);
                }
                case "*", "/" -> {
                    while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/")))
                        queue.add(stack.pop());
                    stack.push(token);
                }
                default -> queue.add(token);
            }
        }

        while (!stack.isEmpty())
            queue.add(stack.pop());

        return queue;
    }

    /**
     * Evaluates a postfix expression
     * @param postfix the postfix expression
     * @return the result
     */
    private static String evaluate(Queue<String> queue) {
        Stack<String> stack = new Stack<>();
        String token;
        while (!queue.isEmpty()) {
            switch (token = queue.poll()) {
                case "+" ->
                    stack.push(String.valueOf(Double.parseDouble(stack.pop()) + Double.parseDouble(stack.pop())));
                case "-" ->
                    stack.push(String.valueOf(-Double.parseDouble(stack.pop()) + Double.parseDouble(stack.pop())));
                case "*" ->
                    stack.push(String.valueOf(Double.parseDouble(stack.pop()) * Double.parseDouble(stack.pop())));
                case "/" ->
                    stack.push(String.valueOf(1 / Double.parseDouble(stack.pop()) * Double.parseDouble(stack.pop())));
                default -> stack.push(token);
            }
        }
        return stack.pop();
    }
}
