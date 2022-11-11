import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter an expression or quit with q: ");
            String expression = scanner.nextLine();// .replaceAll("\\s", "");
            if (expression.equals("q"))
                break;
            System.out.println("The result is: " + calculate(expression));
        }
    }

    private static String calculate(String expression) {
        Queue<String> queue = new ArrayDeque<>();
        Stack<String> stack = new Stack<>();

        String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])");

        for (String token : tokens) {
            switch (token) {
                case "(":
                    stack.push(token);
                    break;
                case ")":
                    while (!stack.peek().equals("(")) {
                        queue.add(stack.pop());
                    }
                    stack.pop();
                    break;
                case "+":
                case "-":
                    while (!stack.isEmpty() && !stack.peek().equals("(")) queue.add(stack.pop());
                    stack.push(token);
                    break;
                case "*":
                case "/":
                    while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/"))) queue.add(stack.pop());
                    stack.push(token);
                    break;
                default:
                    queue.add(token);
            }
        }

        while (!stack.isEmpty()) queue.add(stack.pop());

        return evaluate(queue);
    }

    private static String evaluate(Queue<String> queue) {
        Stack<String> stack = new Stack<>();
        String token;
        while (!queue.isEmpty()) {
            switch (token = queue.poll()) {
                case "+":
                    stack.push(String.valueOf(Double.parseDouble(stack.pop()) + Double.parseDouble(stack.pop())));
                    break;
                case "-":
                    stack.push(String.valueOf(-Double.parseDouble(stack.pop()) + Double.parseDouble(stack.pop())));
                    break;
                case "*":
                    stack.push(String.valueOf(Double.parseDouble(stack.pop()) * Double.parseDouble(stack.pop())));
                    break;
                case "/":
                    stack.push(String.valueOf(1 / Double.parseDouble(stack.pop()) * Double.parseDouble(stack.pop())));
                    break;
                default:
                    stack.push(token);
            }

        }
        return stack.pop();
    }

}
