import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Class validates the expression and variables.
 */
public class Validator {
    public static final String RED_COLOR = "\u001B[31m";
    public static final String RESET_COLOR = "\u001B[0m";
    private final Set functions;
    private String errorMessage;
    private Boolean isValid;

    /**
     * Constructor for the Validator class.
     * Initializes the error message and validation to true.
     */
    public Validator() {
        this.errorMessage = "";
        this.isValid = true;
        this.functions = Set.of("sin", "cos", "tan", "sqrt", "log", "ln");
    }

    /**
     * Method checks if the expression is an assingment.
     * 
     * @param parts the expression split by the equals sign
     * @return the error message if the expression is invalid
     */
    public String validateAssignment(String[] parts) {
        if (!isVariable(parts[0])) {
            errorMessage = "Variable name is invalid. ";
        }
        if (parts.length > 2) {
            errorMessage += "You can only assign one variable at a time. ";
        }
        if (parts[0].substring(0, 1).equals("-")) {
            errorMessage += "Can not assign negative variables.";
        }
        return this.errorMessage;
    }

    /**
     * Method checks if the expression is valid.
     * 
     * @param queue     the expression in queue form
     * @param variables the HashMap of variables
     * @return true if the expression is valid
     */
    public boolean validateExpression(Queue<String> queue, HashMap<String, Double> variables) {
        validateVariables(queue);
        validateAssignments(queue, variables);
        validateParentheses(queue);
        validateOrder(queue);

        return this.isValid;
    }

    /**
     * Method validates the order of tokens in the expression.
     * 
     * @param queue the expression in queue form
     */
    private void validateOrder(Queue<String> queue) {
        Boolean correctOrder = true;
        Queue<String> copy = new ArrayDeque<>(queue);
        String firstToken = copy.poll();
        String token;

        StringBuilder report = new StringBuilder();

        if (isOperator(firstToken)) {
            if (!copy.isEmpty())
                if (!firstToken.equals("-") || !isVariable(copy.peek())) {
                    report.append(RED_COLOR);
                    correctOrder = false;
                }
        }
        report.append(firstToken);
        if (!correctOrder)
            report.append(RESET_COLOR);

        while (!copy.isEmpty()) {
            token = copy.poll();
            if ((isNumber(firstToken) || isVariable(firstToken) || firstToken.equals(")"))
                    && (!isOperator(token) && !token.equals(")"))) {
                report.append(RED_COLOR + token + RESET_COLOR);
                correctOrder = false;
            } else if ((isFunction(firstToken) || firstToken.equals("(")) && isOperator(token)) {
                report.append(RED_COLOR + token + RESET_COLOR);
                correctOrder = false;
            } else if (firstToken.equals("(") && token.equals(")")) {
                report.deleteCharAt(report.length() - 1);
                report.append(RED_COLOR + "(_)" + RESET_COLOR);
                correctOrder = false;
            } else if (isOperator(firstToken) && isOperator(token)) {
                report.append(RED_COLOR + token + RESET_COLOR);
                correctOrder = false;
            } else
                report.append(token);
            firstToken = token;
        }

        if (isOperator(firstToken) || isFunction(firstToken)) {
            report.append(RED_COLOR + "___" + RESET_COLOR);
            correctOrder = false;
        }
        if (!correctOrder)
            errorMessage += "Invalid order of operations or a missing token:\n" + report + "\n";

        if (!correctOrder)
            this.isValid = false;

    }

    /**
     * Method validates that parentheses are balanced.
     * 
     * @param queue the expression in queue form
     */
    private void validateParentheses(Queue<String> queue) {
        Boolean isBalanced = true;
        Stack<String> stack = new Stack<>();
        StringBuilder unbalancedParentheses = new StringBuilder();
        for (String token : queue) {
            if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                if (stack.isEmpty()) {
                    if (isBalanced) {
                        errorMessage += "Too many closing parentheses:\n" + unbalancedParentheses;
                        isBalanced = false;
                    }
                    unbalancedParentheses.append(RED_COLOR + token + RESET_COLOR);
                    this.isValid = false;
                    continue;
                } else
                    stack.pop();
            }
            unbalancedParentheses.append(token);
        }
        if (!isBalanced)
            errorMessage += unbalancedParentheses + "\n";
        if (!stack.isEmpty()) {
            errorMessage += "There are " + stack.size() + " unclosed parentheses.\n";
            this.isValid = false;
        }
    }

    /**
     * Method validates that the variables are in valid format.
     * 
     * @param queue the expression in queue form
     */
    private void validateVariables(Queue<String> queue) {
        for (String token : queue) {
            if (!isNumber(token) && !isOperator(token) && !isFunction(token) && !isVariable(token)
                    && !isParenthesis(token)) {
                errorMessage += "Invalid variable name: " + token + "\n";
                this.isValid = false;
                String[] parts = token.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                if (parts.length > 1) {
                    if (isNumber(parts[0]) && (isVariable(parts[1]) || isFunction(parts[1]))) {
                        errorMessage += "Did you mean " + parts[0] + " * " + parts[1] + "?\n";
                    }
                }
            }
        }
    }

    /**
     * Method validates that the variables in the expression are assigned a value.
     * 
     * @param queue     the expression in queue form
     * @param variables the HashMap of variables
     */
    private void validateAssignments(Queue<String> queue, HashMap<String, Double> variables) {
        String absoluteValue = "";
        for (String token : queue) {
            if (token.length() > 1)
                absoluteValue = (token.charAt(0) == '-') ? token.substring(1) : token;
            else
                absoluteValue = token;
            if (isVariable(absoluteValue) && !variables.containsKey(absoluteValue) && !isFunction(absoluteValue)) {
                errorMessage += "Variable " + token + " is not assigned a value.\n";
                isValid = false;
            }
        }
    }

    public boolean isVariable(String string) {
        if (isFunction(string))
            return false;
        return string.matches("[-]?[a-zA-Z]{1,3}[0-9]{0,4}");
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
     * Checks if the given token is a parenthesis.
     * 
     * @param token the token to check
     * @return true if the token is a parenthesis, false otherwise
     */
    private static boolean isParenthesis(String token) {
        return (token.equals("(") || token.equals(")"));
    }

    /**
     * Checks if the given token is a supported function.
     * 
     * @param token the token to check
     * @return true if the token is a function, false otherwise
     */
    private boolean isFunction(String token) {
        return this.functions.contains(token)
                || (token.charAt(0) == '-' && this.functions.contains(token.substring(1)));
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

    /**
     * Method returns a message of the errors in the expression.
     * 
     * @return the error message
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

}
