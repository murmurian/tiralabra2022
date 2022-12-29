import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.junit.jupiter.api.Test;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class TokenizerTest {
    private Tokenizer tokenizer = new Tokenizer();

    @Test
    public void basicTokenizerSplitTest() {
        String expression = "1+2-3*4/5^6(7)8";
        Queue<String> expected = new ArrayDeque<>(Arrays.asList("1", "+", "2", "-", "3", "*", "4", "/", "5", "^", "6", "(", "7", ")", "8"));
        Queue<String> actual = tokenizer.tokenize(expression);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void functionSplitTest() {
        String expression = "-ln(1)+cos-1*-sin(-2)";
        Queue<String> expected = new ArrayDeque<>(Arrays.asList("-ln", "(", "1", ")", "+", "cos", "-1", "*", "-sin", "(", "-2", ")"));
        Queue<String> actual = tokenizer.tokenize(expression);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void variableSplitTest() {
        String expression = "x+-y1*xyz123/error";
        Queue<String> expected = new ArrayDeque<>(Arrays.asList("x", "+", "-y1", "*", "xyz123", "/", "error"));
        Queue<String> actual = tokenizer.tokenize(expression);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void singleVariableTest() {
        String expression = "x";
        Queue<String> expected = new ArrayDeque<>(Arrays.asList("x"));
        Queue<String> actual = tokenizer.tokenize(expression);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void negativeValueTest() {
        String expression = "-1";
        Queue<String> expected = new ArrayDeque<>(Arrays.asList("-1"));
        Queue<String> actual = tokenizer.tokenize(expression);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void allFeaturesTest() {
        String expression = "-(-sin(45)+x3^2)test1337";
        Queue<String> expected = new ArrayDeque<>(Arrays.asList("-1","*", "(", "-sin", "(", "45", ")", "+", "x3", "^", "2", ")", "test1337"));
        Queue<String> actual = tokenizer.tokenize(expression);
        assertIterableEquals(expected, actual);
    }

    @Test
    public void unbalancedParenthesesTest() {
        String expression = "-(";
        Queue<String> expected = new ArrayDeque<>(Arrays.asList("-1", "*","("));
        Queue<String> actual = tokenizer.tokenize(expression);
        assertIterableEquals(expected, actual);
    }
    
    @Test
    public void compositeFunctionTest() {
        String expression = "sqrtcostan1*sinx";
        Queue<String> expected = new ArrayDeque<>(Arrays.asList("sqrt", "cos", "tan", "1", "*", "sin", "x"));
        Queue<String> actual = tokenizer.tokenize(expression);
        assertIterableEquals(expected, actual);
    }
}
