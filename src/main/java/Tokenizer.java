import java.util.ArrayDeque;
import java.util.ArrayList;
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

    private static ArrayDeque<String> parseNegativeValues(Queue<String> function) {
        ArrayDeque<String> result = new ArrayDeque<>();

        /*
        String firstToken = "";
        String secondToken = "";

        while (function.peek().equals("-")) {
            function.remove();
            if (isNumber(function.peek())) {
                result.add("-" + function.poll());
                break;
            } else {
                result.add("-1");
                result.add("*");
                if (function.peek().equals("-"))
                    continue;
                result.add(function.poll());
            }
        }

        for (String token : function) {
            if ((isOperator(token) || isFunction(token)) && firstToken.equals("")) {
                firstToken = token;
                result.add(token);
                continue;
            } else if ((isOperator(firstToken) || isFunction(firstToken)) && secondToken.equals("-")
                    && (isNumber(token) || isFunction(token))) {
                result.removeLast();
                result.add("-" + token);
                firstToken = "";
            } else if (isOperator(firstToken) && isFunction(secondToken)) {
                result.removeLast();
                result.add("-" + token);
                firstToken = "";
                secondToken = "";
                continue;
            } else {
                result.add(token);
            }
            secondToken = token;
        }*/

        System.out.println("input: " + function.toString());

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

        for (String token : function) {
            if (isParentheses(token)) {
                result.add(token);
                continue;
            }
            if ((isOperator(firstToken) || firstToken.equals("(")) && secondToken.equals("-") && (isNumber(token) || isFunction(token))) {
                result.removeLast();
                result.add("-" + token);
            } else if (isFunction(firstToken) && secondToken.equals("-") && (isNumber(token) || isFunction(token))) {
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
}
