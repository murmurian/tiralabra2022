import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;

/**
 * Class validates the expression and variables.
 */
public class Validator {
    private String errorMessage;
    private Boolean isValid;

    /**
     * Constructor for the Validator class.
     * Initializes the error message and validation to true.
     */
    public Validator() {
        this.errorMessage = "";
        this.isValid = true;
    }

    /**
     * Method checks if the expression is an assingment.
     * 
     * @param parts the expression split by the equals sign
     * @return the error message if the expression is invalid
     */
    public String validateAssignment(String[] parts) {
        if (parts.length > 2) {
            errorMessage = "You can only assign one variable at a time. ";
        }
        if (parts[0].substring(0, 1).equals("-")) {
            errorMessage += "Can not assign negative variables. ";
        }
        if (!isVariable(parts[0])) {
            errorMessage += "Variable name is invalid.";
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
        Queue<String> copy = new ArrayDeque<>(queue);
        String firstToken = copy.poll();
        String token;

        StringBuilder report = new StringBuilder();
        report.append(firstToken);

        while (!copy.isEmpty()) {
            token = copy.poll();
            if ((isNumber(firstToken) || isVariable(firstToken) || firstToken.equals(")"))
                    && (!isOperator(token) && !token.equals(")"))) {
                errorMessage += "Invalid order of operations: " + report + " @ " + token + " @ " + "\n";
                this.isValid = false;
            }
            if ((isFunction(firstToken) || firstToken.equals("(")) && isOperator(token)) {
                errorMessage += "Invalid order of operations: " + report + " @ " + token + " @ " + "\n";
                this.isValid = false;
            }
            if (firstToken.equals("(") && token.equals(")")) {
                errorMessage += "Missing value or variable: " + report + " @ " + token + "\n";
                this.isValid = false;
            }
            if (isOperator(firstToken) && isOperator(token)) {
                errorMessage += "Invalid order of operations: " + report + " @ " + token + " @ " + "\n";
                this.isValid = false;
            }
            report.append(token);
            firstToken = token;
        }

        if (isOperator(firstToken) || isFunction(firstToken)) {
            errorMessage += "Missing value or variable: " + report + " @ " + "\n";
            this.isValid = false;
        }

    }

    /**
     * Method validates that parentheses are balanced.
     * 
     * @param queue the expression in queue form
     */
    private void validateParentheses(Queue<String> queue) {
        Stack<String> stack = new Stack<>();
        StringBuilder unbalancedParentheses = new StringBuilder();
        for (String token : queue) {
            if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                if (stack.isEmpty()) {
                    errorMessage += "Unbalanced parentheses at " + unbalancedParentheses + " @ " + token + " @ " + "\n";
                    this.isValid = false;
                } else {
                    stack.pop();
                }
            }
            unbalancedParentheses.append(token);
        }
        if (!stack.isEmpty()) {
            errorMessage += "All parentheses are not closed.";
            this.isValid = false;
        }
    }

    /**
     * Method validates that the variables are in valid format.
     * 
     * @param queue the expression in queue form
     */
    private void validateVariables(Queue<String> queue) {
        StringBuilder report = new StringBuilder();
        for (String token : queue) {
            if (!isNumber(token) && !isOperator(token) && !isFunction(token) && !isVariable(token)
                    && !isParenthesis(token)) {
                // Split the token with with regex to double and string
                String[] parts = token.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                if (parts.length > 1) {
                    if (isNumber(parts[0]) && (isVariable(parts[1]) || isFunction(parts[1]))) {
                        errorMessage += "Did you mean " + report + parts[0] + " * " + parts[1] + "?\n";
                    }
                } else
                    errorMessage += "Invalid variable name: " + token + "\n";
                this.isValid = false;
            }            
            report.append(token);
        }
    }

    /**
     * Method validates that the variables in the expression are assigned a value.
     * 
     * @param queue     the expression in queue form
     * @param variables the HashMap of variables
     */
    private void validateAssignments(Queue<String> queue, HashMap<String, Double> variables) {
        for (String token : queue) {
            if (isVariable(token) && !variables.containsKey(token) && !isFunction(token)) {
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
    private static boolean isFunction(String token) {
        return token.equals("sin") || token.equals("cos") || token.equals("tan") || token.equals("-sin")
                || token.equals("-cos") || token.equals("-tan") || token.equals("sqrt") || token.equals("-sqrt");
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
