import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/*
 * It seems unnecessary to thoroughly test this class, as most of the methods are
 * private and are better tested through end-to-end testing. However, I will
 * use this class to try out injecting IO-dependecy into the class under test.
 */
public class CalculatorTest {
    private Calculator calculator = new Calculator(new TextIO());

    @Test
    public void angleSetsToRadians() {
        IO io = new TestIO("rad");
        calculator.setAngle("rad", io);
        assertEquals("Angle mode set to radians", io.getOutput());
    }

    @Test
    public void angleSetsToDegrees() {
        IO io = new TestIO("deg");
        calculator.setAngle("deg", io);
        assertEquals("Angle mode set to degrees", io.getOutput());
    }

    @Test
    public void emptyExpressionPasses() {
        String expected = "Expression is empty.";
        String actual = calculator.calculate("");
        assertEquals(expected, actual);
    }

    @Test
    public void emptyAssignmentFails() {
        String expected = "Variable name is empty.";
        String actual = calculator.calculate("=");
        assertEquals(expected, actual);
        actual = calculator.calculate("===");
        assertEquals(expected, actual);
    }

    @Test
    public void validAssignmentPasses() {
        String expected = "Variable x assigned to 1337.0";
        String actual = calculator.calculate("x=1337");
        assertEquals(expected, actual);
    }

    @Test
    public void tooManyEqualsFails() {
        String expected = "You can only assign one variable at a time. ";
        String actual = calculator.calculate("x=1337=42069");
        assertEquals(expected, actual);
    }

    @Test
    public void invalidVariableFails() {
        String expected = "Variable name is invalid. ";
        String actual = calculator.calculate("testVar=1337");
        assertEquals(expected, actual);
    }

    @Test
    public void negativeAssignmentFails() {
        String expected = "Can not assign negative variables.";
        String actual = calculator.calculate("-x=1337");
        assertEquals(expected, actual);
    }

    @Test
    public void feedBackForInvalidVariable() {
        String expected = "Invalid variable name: 1337x\nDid you mean 1337 * x?\n";
        String actual = calculator.calculate("1337x");
        assertEquals(expected, actual);
    }

    @Test
    public void assignedVariableIsCorrect() {
        calculator.calculate("x=1337");
        String expected = "1337.0";
        String actual = calculator.calculate("x");
        assertEquals(expected, actual);
    }

    @Test
    public void unAssignedVariableFails() {
        String expected = "Variable x is not assigned a value.\n";
        String actual = calculator.calculate("x");
        assertEquals(expected, actual);
    }

    @Test
    public void balancedParenthesisPasses() {
        String expected = "9.0";
        String actual = calculator.calculate("(1+2)*3");
        assertEquals(expected, actual);
    }

    @Test
    public void unclosedParenthesisFails() {
        String expected = "There are 1 unclosed parentheses.\n";
        String actual = calculator.calculate("(1+2)*(3");
        assertEquals(expected, actual);
    }

    @Test
    public void unopenedParenthesisFails() {
        String expected = "Too many closing parentheses:\n1+2[31m)[0m*3[31m)[0m\n";
        String actual = calculator.calculate("1+2)*3)");
        assertEquals(expected, actual);
    }

    @Test
    public void emptyParenthesisFails() {
        String expected = "Invalid order of operations or a missing token:\n[31m(_)[0m\n";
        String actual = calculator.calculate("()");
        assertEquals(expected, actual);
    }

    @Test
    public void incorrectOrder() {
        String expected = "Invalid order of operations or a missing token:\n[31m*[0m3\n";
        String actual = calculator.calculate("*3");
        assertEquals(expected, actual);
    }

    @Test
    public void incorrectOrder2() {
        String expected = "Invalid order of operations or a missing token:\n(1)[31m5[0m\n";
        String actual = calculator.calculate("(1)5");
        assertEquals(expected, actual);
    }

    @Test
    public void incorrectOrder3() {
        String expected = "Invalid order of operations or a missing token:\n1+sin[31m+[0m5\n";
        String actual = calculator.calculate("1+sin+5");
        assertEquals(expected, actual);
    }

    @Test
    public void doubleOperatorFails() {
        String expected = "Invalid order of operations or a missing token:\n1+[31m+[0m5\n";
        String actual = calculator.calculate("1++5");
        assertEquals(expected, actual);
    }

    @Test
    public void expressionEndsWithOperator() {
        String expected = "Invalid order of operations or a missing token:\n1+2*[31m___[0m\n";
        String actual = calculator.calculate("1+2*");
        assertEquals(expected, actual);
    }

    @Test
    void multipleErrorsFails() {
        String expected = "Variable x is not assigned a value.\nToo many closing parentheses:\n1+2(*sqrt/)+[31m)[0m[31m)[0mx(\nThere are 1 unclosed parentheses.\nInvalid order of operations or a missing token:\n1+2[31m([0m[31m*[0msqrt[31m/[0m)+))[31mx[0m[31m([0m\n";
        String actual = calculator.calculate("1+2(*sqrt/)+))x(");
        assertEquals(expected, actual);
    }

    // Now for the actual calculation tests

    @Test
    public void basicOperations() {
        String expected = "7.0";
        String actual = calculator.calculate("1+2*3/4^0");
        assertEquals(expected, actual);
    }

    @Test
    public void trigonometricIdentity() {
        String expected = "1.0";
        String actual = calculator.calculate("sin(0)^2+cos(0)^2");
        assertEquals(expected, actual);
    }

    @Test
    public void compositeTrigonometricFunction() {
        String expected = "0.0";
        String actual = calculator.calculate("tansin(0)");
        assertEquals(expected, actual);
    }

    @Test
    public void degreesWork() {
        calculator.setAngle("deg", new TextIO());
        String expected = "-1.0";
        String actual = calculator.calculate("cos(180)");
        assertEquals(expected, actual);
    }

    @Test
    public void assignedValuesWork() {
        calculator.calculate("x=1337");
        String expected = "1337.0";
        String actual = calculator.calculate("x");
        assertEquals(expected, actual);
    }

    @Test
    public void assignedValuesWork2() {
        calculator.calculate("x=1337");
        calculator.calculate("y=42069");
        String expected = "43406.0";
        String actual = calculator.calculate("x--y");
        assertEquals(expected, actual);
    }

    @Test
    public void assignedValuesWork3() {
        calculator.calculate("x=-9");
        String expected = "6.0";
        String actual = calculator.calculate("-x-sqrt-x");
        assertEquals(expected, actual);
    }

    @Test
    public void assignedValuesWork4() {
        calculator.calculate("x=-1337");
        String expected = "Variable x assigned to 1337.0";
        String actual = calculator.calculate("x=-x");
        assertEquals(expected, actual);
    }

    @Test
    public void logWorks() {
        String expected = "2.0";
        String actual = calculator.calculate("log(100)");
        assertEquals(expected, actual);
    }

    @Test
    public void lnWorks() {
        String expected = "2.0";
        String actual = calculator.calculate("ln(e^2)");
        assertEquals(expected, actual);
    }

    @Test
    public void logFailsNegative() {
        TestIO testIO = new TestIO("log(-1337)");
        Calculator ioCalculator = new Calculator(testIO);
        String expected = "Logarithm of a negative number is not defined.";
        ioCalculator.calculate(testIO.readLine());
        String actual = testIO.getOutput();
        assertEquals(expected, actual);
    }

    @Test
    public void lnFailsNegative() {
        TestIO testIO = new TestIO("ln(-42069)");
        Calculator ioCalculator = new Calculator(testIO);
        String expected = "Logarithm of a negative number is not defined.";
        ioCalculator.calculate(testIO.readLine());
        String actual = testIO.getOutput();
        assertEquals(expected, actual);
    }

}
