import java.util.ArrayDeque;
import java.util.Queue;

public class Tokenizer {
    /**
     * Tokenizes the given expression into a queue of Strings representing supported
     * operators, operands and functions.
     * 
     * @param expression the expression to tokenize
     * @return a queue of Strings representing the expression
     */
    public Queue<String> tokenize(String expression) {
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
