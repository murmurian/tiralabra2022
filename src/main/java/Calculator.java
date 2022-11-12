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
    public static Queue<String> infixToPostfix(String expression) {
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
     * @param queue the postfix expression
     * @return the result
     */
    public static Double evaluate(Queue<String> queue) {
        Stack<Double> stack = new Stack<>();

        while (!queue.isEmpty()) {
            String token = queue.remove();
            if (isNumber(token))
                stack.push(Double.parseDouble(token));
            else {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                switch (token) {
                    case "+" -> stack.push(operand1 + operand2);
                    case "-" -> stack.push(operand1 - operand2);
                    case "*" -> stack.push(operand1 * operand2);
                    case "/" -> stack.push(operand1 / operand2);
                }
            }
        }

        return stack.pop();
    }

    /**
     * Checks if a token is a number
     * @param token the string to check
     * @return true if the string is a number, false otherwise
     */
    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
