import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import org.junit.jupiter.api.Test;

public class CalculatorTest {
    @Test
    public void firstTest() {
        assertTrue(true);
    }

    @Test
    public void infixToPostfixWorks() {
        Queue<String> result = new ArrayDeque<>(Arrays.asList("1", "2", "+"));
        assertIterableEquals(result, Calculator.infixToPostfix("1+2"));
        result = new ArrayDeque<>(Arrays.asList("1", "2", "3", "*", "+"));
        assertIterableEquals(result, Calculator.infixToPostfix("1+2*3"));
        result = new ArrayDeque<>(Arrays.asList("1", "2", "*", "3", "+"));
        assertIterableEquals(result, Calculator.infixToPostfix("(1*2)+3"));
    }
    
    @Test
    public void evaluateWorks() {
        Queue<String> postfix = new ArrayDeque<>(Arrays.asList("1", "2", "+"));
        assertTrue(Calculator.evaluate(postfix) == 3);
        postfix = new ArrayDeque<>(Arrays.asList("1", "2", "3", "*", "+"));
        assertTrue(Calculator.evaluate(postfix) == 7);
        postfix = new ArrayDeque<>(Arrays.asList("1", "2", "*", "3", "+"));
        assertTrue(Calculator.evaluate(postfix) == 5);
    }

    @Test
    public void calculatorWorks() {
        assertTrue(Calculator.evaluate(Calculator.infixToPostfix("1+2")) == 3);
        assertTrue(Calculator.evaluate(Calculator.infixToPostfix("1+2*3")) == 7);
        assertTrue(Calculator.evaluate(Calculator.infixToPostfix("(1+2)*3")) == 9);
    }
}
