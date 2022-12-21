import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;

public class Validator {
    private String errorMessage;
    private Boolean isValid;

    public Validator() {
        this.errorMessage = "";
        this.isValid = true;
    }

    public String validateAssignment(String[] parts) {
        if (parts.length > 2) {
            errorMessage = "You can only assign one variable at a time. ";
        }
        if (!isVariable(parts[0])) {
            errorMessage += "Variable name is invalid.";
        }
        return this.errorMessage;
    }

    public boolean ValidateExpression(Queue<String> queue, HashMap<String, Double> variables) {
        validateVariables(queue);
        validateAssignments(queue, variables);
        validateParentheses(queue);
        //validateOrder(queue);

        return this.isValid;
    }

    private void validateOrder(Queue<String> queue) {
        String previousToken = "";
        for (String token : queue) {
            if (isOperator(token) && isOperator(previousToken) && !previousToken.equals(")") && !token.equals("(") && !previousToken.equals("-")) {
                errorMessage += "Invalid order of operators: " + previousToken + " " + token + "\n";
                this.isValid = false;
            }
            previousToken = token;
        }
    }

    private void validateParentheses(Queue<String> queue) {
        Stack<String> stack = new Stack<>();
        StringBuilder unbalancedParentheses = new StringBuilder();
        for (String token : queue) {
            if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                if (stack.isEmpty()) {
                    errorMessage += "Unbalanced parentheses at " + unbalancedParentheses + "___" + token + "___";
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

    private void validateVariables(Queue<String> queue) {        
        for (String token : queue) {
            if (!isNumber(token) && !isOperator(token) && !isFunction(token) && !isVariable(token)) {
                errorMessage += "Invalid variable name: " + token + "\n";
                this.isValid = false;
            }
        }
    }

    private void validateAssignments(Queue<String> queue, HashMap<String, Double> variables) {
        for (String token : queue) {
            if (isVariable(token) && !variables.containsKey(token) && !isFunction(token)) {
                errorMessage += "Variable " + token + " is not assigned a value.\n";
                isValid = false;
            }
        }
    }

    public boolean isVariable(String string) {
        return string.matches("[a-zA-Z]{1,3}[0-9]{0,4}");
    }

    /**
     * Checks if the given token is an operator.
     * 
     * @param token the token to check
     * @return true if the token is an operator, false otherwise
     */
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^") || token.equals("(") || token.equals(")");
    }

    /**
     * Checks if the given token is a supported function.
     * 
     * @param token the token to check
     * @return true if the token is a function, false otherwise
     */
    private static boolean isFunction(String token) {
        return token.equals("sin") || token.equals("cos") || token.equals("tan") || token.equals("-sin") || token.equals("-cos") || token.equals("-tan");
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

    public String getErrorMessage() {
        return this.errorMessage;
    }

}
