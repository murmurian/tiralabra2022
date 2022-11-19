import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {
    public static void main(String[] args) {

        // TODO: Dependency injection for the scanner
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter an expression or quit with q: ");
            String expression = scanner.nextLine().replaceAll("\\s", "");
            if (expression.equals("q"))
                break;
            Queue<String> infix = tokenize(expression);

            // TODO: Validate the infix expression

            Queue<String> postfix = infixToPostfix(infix);
            System.out.println("The result is: " + evaluate(postfix));
        }
    }

    /**
     * Tokenizes the given expression into a queue of Strings representing supported
     * operators, operands and functions.
     * 
     * @param expression the expression to tokenize
     * @return a queue of Strings representing the expression
     */
    public static Queue<String> tokenize(String expression) {
        Queue<String> infix = new ArrayDeque<>();
        String[] tokens = expression.split("(?<=[-+*/()^])|(?=[-+*/()^])");

        for (String token : tokens) {
            if (isOperator(token) || isParentheses(token) || isNumber(token)) {
                infix.add(token);
            } else if (token.length() > 2) {
                infix.addAll(tokenizeFunction(token));
            } else {
                infix.add(token);
            }
        }
        return infix;
    }

    /**
     * Recurcisvely tokenizes a String into a queue of functions. Supports composite
     * functions.
     * 
     * @param token the function to tokenize
     * @return a queue of Strings representing the function
     */
    private static ArrayDeque<String> tokenizeFunction(String token) {
        ArrayDeque<String> function = new ArrayDeque<>();
        String functionToken = token.substring(0, 3);

        if (isFunction(functionToken)) {
            function.add(functionToken);
            if (isNumber(token.substring(3)))
                function.add(token.substring(3));
            else if (token.substring(3).length() > 2)
                function.addAll(tokenizeFunction(token.substring(3)));
            else if (token.substring(3).length() != 0)
                function.add(token.substring(3));
        } else
            function.add(token);

        return function;
    }

    /**
     * Checks if the given token is a parenthesis.
     * 
     * @param token the token to check
     * @return true if the token is a parenthesis, false otherwise
     */
    private static boolean isParentheses(String token) {
        return token.equals("(") || token.equals(")");
    }

    /**
     * Checks if the given token is an operator.
     * 
     * @param token the token to check
     * @return true if the token is an operator, false otherwise
     */
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^");
    }

    /**
     * Checks if the given token is a supported function.
     * 
     * @param token the token to check
     * @return true if the token is a function, false otherwise
     */
    private static boolean isFunction(String token) {
        return token.equals("sin") || token.equals("cos") || token.equals("tan");
    }

    /**
     * Converts an infix expression to a postfix expression
     * 
     * @param expression the infix expression
     * @return the postfix as a queue expression
     */
    public static Queue<String> infixToPostfix(Queue<String> infix) {
        Queue<String> queue = new ArrayDeque<>();
        Stack<String> stack = new Stack<>();

        for (String token : infix) {
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
                case "^" -> {
                    while (!stack.isEmpty() && stack.peek().equals("^"))
                        queue.add(stack.pop());
                    stack.push(token);
                }
                default -> {
                    if (isFunction(token))
                        stack.push(token);
                    else
                        queue.add(token);
                }
            }
        }

        while (!stack.isEmpty())
            queue.add(stack.pop());

        return queue;
    }

    /**
     * Evaluates a postfix expression
     * 
     * @param queue the postfix expression
     * @return the result
     */
    public static Double evaluate(Queue<String> queue) {
        Stack<Double> stack = new Stack<>();

        // TODO: Implement strategy design pattern?
        while (!queue.isEmpty()) {
            String token = queue.remove();
            if (isNumber(token))
                stack.push(Double.parseDouble(token));
            else if (isOperator(token)) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                switch (token) {
                    case "+" -> stack.push(operand1 + operand2);
                    case "-" -> stack.push(operand1 - operand2);
                    case "*" -> stack.push(operand1 * operand2);
                    case "/" -> stack.push(operand1 / operand2);
                    default -> stack.push(Math.pow(operand1, operand2));
                }
            } else if (isFunction(token)) {
                double operand = stack.pop();
                switch (token) {
                    case "sin" -> stack.push(Math.sin(operand));
                    case "cos" -> stack.push(Math.cos(operand));
                    default -> stack.push(Math.tan(operand));
                }
            }
        }

        return stack.pop();
    }

    private static Double evaluateFunction(String token, double operand1) {
        return null;
    }

    /**
     * Checks if a token is a number
     * 
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
