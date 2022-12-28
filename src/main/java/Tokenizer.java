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
        infix = parseNegativeValues(infix);
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
        System.out.println(token);
        ArrayDeque<String> function = new ArrayDeque<>();
        int functionLength = 2;
        for (int i = 2; i <= token.length(); i++)
            if (isFunction(token.substring(0, i)))
                functionLength = i;

        if (isFunction(token.substring(0, functionLength))) {
            function.add(token.substring(0, functionLength));
            if (isNumber(token.substring(functionLength)))
                function.add(token.substring(functionLength));
            else if (token.substring(functionLength).length() > 1)
                function.addAll(tokenizeFunction(token.substring(functionLength)));
            else if (token.substring(functionLength).length() != 0)
                function.add(token.substring(functionLength));
        } else {
            function.add(token);
        }

        return function;
    }

    /**
     * Parses negative values and adds a negative sign to the number or function
     * 
     * @param function the function to parse
     * @return a queue of Strings representing the function
     */
    private static ArrayDeque<String> parseNegativeValues(Queue<String> function) {
        ArrayDeque<String> result = new ArrayDeque<>();

        if (function.size() == 1) {
            result.add(function.peek());
            return result;
        }

        result.add(function.peek());
        String firstToken = function.poll();
        result.add(function.peek());
        String secondToken = function.poll();

        if (firstToken.equals("-") && (isNumber(secondToken) || isFunction(secondToken))) {
            result.removeLast();
            result.removeLast();
            result.add("-" + secondToken);
            if (function.size() == 0)
                return result;
            firstToken = secondToken;
            secondToken = function.peek();
        }

        if (firstToken.equals("-") && secondToken.equals("(")) {
            result.removeLast();
            result.removeLast();
            result.add("-1");
            result.add("*");
            result.add("(");
            if (function.size() == 0)
                return result;
            firstToken = secondToken;
            secondToken = function.peek();
        }

        for (String token : function) {
            if (isParentheses(token)) {
                result.add(token);
                firstToken = secondToken;
                secondToken = token;
                continue;
            }
            if ((isOperator(firstToken) || firstToken.equals("(")) && secondToken.equals("-")
                    && (isNumber(token) || isFunction(token) || isVariable(token))) {
                result.removeLast();
                result.add("-" + token);
            } else if (isFunction(firstToken) && secondToken.equals("-")
                    && (isNumber(token) || isFunction(token) || isVariable(token))) {
                result.removeLast();
                result.add("-" + token);
            } else
                result.add(token);
            firstToken = secondToken;
            secondToken = token;
        }

        return result;
    }

    /**
     * Checks if the given token is a possible variable.
     * 
     * @param token the token to check
     * @return true if the token is a variable, false otherwise
     */
    private static boolean isVariable(String token) {
        return (!isOperator(token) && !isParentheses(token) && !isNumber(token) && !isFunction(token));
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
}
