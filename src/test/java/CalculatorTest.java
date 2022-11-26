import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import org.junit.jupiter.api.Test;

public class CalculatorTest {
    private String expression1 = ("1+2");
    private String expression2 = ("1.0+2*3.0");
    private String expression3 = ("(1+2)*3");
    private String trigExpression1 = ("sin(1)");
    private String trigExpression2 = ("sin1.0+cos(2)");
    private String trigExpression3 = ("sincos(2.3)*tanx3");

    private Queue<String> infix1 = new ArrayDeque<>(Arrays.asList("1", "+", "2"));
    private Queue<String> infix2 = new ArrayDeque<>(Arrays.asList("1.0", "+", "2", "*", "3.0"));
    private Queue<String> infix3 = new ArrayDeque<>(Arrays.asList("(", "1", "+", "2", ")", "*", "3"));
    private Queue<String> trigInfix1 = new ArrayDeque<>(Arrays.asList("sin", "(", "1", ")"));
    private Queue<String> trigInfix2 = new ArrayDeque<>(Arrays.asList("sin", "1.0", "+", "cos", "(", "2", ")"));
    private Queue<String> trigInfix3 = new ArrayDeque<>(Arrays.asList("sin", "cos", "(", "2.3", ")", "*", "tan", "x3"));

    private Queue<String> postfix1 = new ArrayDeque<>(Arrays.asList("1", "2", "+"));
    private Queue<String> postfix2 = new ArrayDeque<>(Arrays.asList("1.0", "2", "3.0", "*", "+"));
    private Queue<String> postfix3 = new ArrayDeque<>(Arrays.asList("1", "2", "^", "3", "*"));

    @Test
    public void firstTest() {
        assertTrue(true);
    }
/*
    @Test
    public void testTokenizeBasicArithmetics() {
        assertIterableEquals(infix1, Calculator.tokenize(expression1));
        assertIterableEquals(infix2, Calculator.tokenize(expression2));
        assertIterableEquals(infix3, Calculator.tokenize(expression3));
    }

    @Test
    public void testTokenizeTrigFunctions() {
        assertIterableEquals(trigInfix1, Calculator.tokenize(trigExpression1));
        assertIterableEquals(trigInfix2, Calculator.tokenize(trigExpression2));
        assertIterableEquals(trigInfix3, Calculator.tokenize(trigExpression3));
    }
*/
    @Test
    public void testInfixToPostfixBasic() {
        assertIterableEquals(new ArrayDeque<>(Arrays.asList("1", "2", "+")), Calculator.infixToPostfix(infix1));
        assertIterableEquals(new ArrayDeque<>(Arrays.asList("1.0", "2", "3.0", "*", "+")), Calculator.infixToPostfix(infix2));
        assertIterableEquals(new ArrayDeque<>(Arrays.asList("1", "2", "+", "3", "*")), Calculator.infixToPostfix(infix3));
    }
    
    @Test
    public void testInfixToPostfixTrig() {
        assertIterableEquals(new ArrayDeque<>(Arrays.asList("1", "sin")), Calculator.infixToPostfix(trigInfix1));
        assertIterableEquals(new ArrayDeque<>(Arrays.asList("1.0", "sin", "2", "cos", "+")), Calculator.infixToPostfix(trigInfix2));
    }

    @Test
    public void testEvaluateBasic() {
        assertTrue(Calculator.evaluate(postfix1) == 3);
        assertTrue(Calculator.evaluate(postfix2) == 7);
        assertTrue(Calculator.evaluate(postfix3) == 3);
    }

    @Test
    public void evaluateFalse() {
        assertFalse(Calculator.evaluate(postfix1) == 4);
        assertFalse(Calculator.evaluate(postfix2) == 8);
        assertFalse(Calculator.evaluate(new ArrayDeque<>(Arrays.asList("1", "sin"))) == 1337);
    }

    @Test void testEvaluateTrig() {
        assertTrue(Calculator.evaluate(new ArrayDeque<>(Arrays.asList("1", "sin"))) == Math.sin(1));
        assertTrue(Calculator.evaluate(new ArrayDeque<>(Arrays.asList("1.0", "sin", "2", "cos", "+"))) == Math.sin(1.0) + Math.cos(2));
    }
/*
    @Test
    public void calculatorWorks() {
        assertTrue(Calculator.evaluate(Calculator.infixToPostfix(Calculator.tokenize(expression1))) == 3);
        assertTrue(Calculator.evaluate(Calculator.infixToPostfix(Calculator.tokenize(expression2))) == 7);
        assertTrue(Calculator.evaluate(Calculator.infixToPostfix(Calculator.tokenize(expression3))) == 9);
    }
*/
}
