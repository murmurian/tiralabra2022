import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;

/**
 * Class implements a shunting-yard algorithm to convert an infix expression to
 * postfix. It also evaluates the postfix expression. Class also supports
 * variable assignment.
 */
public class Calculator {
    private HashMap<String, Double> variables;
    private Tokenizer tokenizer;

    /**
     * Constructor for the Calculator class.
     * Intializes a new HashMap for variables and a new Tokenizer.
     */
    public Calculator() {
        this.variables = new HashMap<>();
        this.tokenizer = new Tokenizer();
    }

    /**
     * Method checks if expression is an assignment, tokenizes the expression and
     * validates it. If the expression is valid, it is converted to postfix and
     * evaluated. If the expression is an assignment, the variable is added to the
     * HashMap.
     * 
     * @param expression the expression to evaluate
     * @return the result of the expression
     */
    public String calculate(String expression) {
        if (expression.isEmpty())
            return "Expression is empty.";
        Validator validator = new Validator();
        Queue<String> queue;
        Boolean isAssignment = false;
        String variable = "";
        Double result;

        if (expression.contains("=")) {
            String[] parts = expression.split("=");
            if (parts.length != 2 || !validator.isVariable(parts[0]) || parts[0].substring(0, 1).equals("-")) {
                return validator.validateAssignment(parts);
            }
            variable = parts[0];
            isAssignment = true;
            queue = tokenizer.tokenize(parts[1]);
        } else {
            queue = tokenizer.tokenize(expression);
        }

        if (!validator.ValidateExpression(queue, variables))
            return validator.getErrorMessage();

        queue = parseNegativeExpressions(queue);
        queue = infixToPostfix(queue);
        queue = assignVariables(queue);
        result = evaluate(queue);

        if (isAssignment)
            variables.put(variable, result);

        return (isAssignment) ? "Variable " + variable + " assigned to " + result : String.valueOf(result);
    }

    /**
     * Method checks if the expression is a negative expression and handles it
     * accordingly.
     * 
     * @param queue the queue to parse
     * @return the parsed queue
     */
    private Queue<String> parseNegativeExpressions(Queue<String> queue) {
        Queue<String> result = new ArrayDeque<>();
        for (String token : queue) {
            if (token.length() > 1 && token.charAt(0) == '-' && !Character.isDigit(token.charAt(1))) {
                result.add("-1");
                result.add("*");
                result.add(token.substring(1));
            } else
                result.add(token);
        }
        return result;
    }

    /**
     * Method assigns variables to their values in the expression.
     * 
     * @param postfix
     * @return
     */
    private Queue<String> assignVariables(Queue<String> postfix) {
        Queue<String> result = new ArrayDeque<>();
        for (String token : postfix) {
            if (variables.containsKey(token)) {
                result.add(String.valueOf(variables.get(token)));
            } else if (token.substring(0).equals("-") && variables.containsKey(token.substring(1))) {
                result.add("-" + String.valueOf(variables.get(token.substring(1))));
            } else {
                result.add(token);
            }
        }
        return result;
    }

    /**
     * Converts an infix expression to a postfix expression
     * 
     * @param expression the infix expression
     * @return the postfix as a queue expression
     */
    private static Queue<String> infixToPostfix(Queue<String> infix) {
        Queue<String> queue = new ArrayDeque<>();
        Stack<String> stack = new Stack<>();

        for (String token : infix) {
            if (isFunction(token)) {
                stack.push(token);
                continue;
            }
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
                    while (!stack.isEmpty()
                            && (stack.peek().equals("*") || stack.peek().equals("/") || isFunction(stack.peek())))
                        queue.add(stack.pop());
                    stack.push(token);
                }
                case "^" -> {
                    while (!stack.isEmpty() && (stack.peek().equals("^") || isFunction(stack.peek())))
                        queue.add(stack.pop());
                    stack.push(token);
                }
                default -> {
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
    private static Double evaluate(Queue<String> queue) {
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
                    case "^" -> stack.push(Math.pow(operand1, operand2));
                }
            } else if (isFunction(token)) {
                double operand = stack.pop();
                switch (token) {
                    case "sin" -> stack.push(Math.sin(operand));
                    case "cos" -> stack.push(Math.cos(operand));
                    case "tan" -> stack.push(Math.tan(operand));
                }
            }
        }

        return stack.pop();
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
