import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ValidatorTest {
    private Validator validator = new Validator();

    @Test
    public void validAssignmentPasses() {
        String[] parts = new String[] {"x", "1337"};
        String expected = "";
        String actual = validator.validateAssignment(parts);
        assertEquals(expected, actual);
    }

    @Test
    public void tooManyEqualsFails() {
        String[] parts = new String[] {"x", "42069", "1337"};
        String expected = "You can only assign one variable at a time. ";
        String actual = validator.validateAssignment(parts);
        assertEquals(expected, actual);
    }

    @Test
    public void invalidVariableFails() {
        String[] parts = new String[] {"testVar", "1337"};
        String expected = "Variable name is invalid. ";
        String actual = validator.validateAssignment(parts);
        assertEquals(expected, actual);
    }

    @Test
    public void negativeAssignmentFails() {
        String[] parts = new String[] {"-x", "1337"};
        String expected = "Can not assign negative variables.";
        String actual = validator.validateAssignment(parts);
        assertEquals(expected, actual);
    }

    @Test
    public void multipleErrorsFails() {
        String[] parts = new String[] {"-testVar", "1337", "42069"};
        String expected = "Variable name is invalid. You can only assign one variable at a time. Can not assign negative variables.";
        String actual = validator.validateAssignment(parts);
        assertEquals(expected, actual);
    }

    @Test
    public void isVariablePasses() {
        String variable = "x1337";
        boolean expected = true;
        boolean actual = validator.isVariable(variable);
        assertEquals(expected, actual);
    }

    @Test
    public void isVariableFails() {
        String variable = "testVar42069";
        boolean expected = false;
        boolean actual = validator.isVariable(variable);
        assertEquals(expected, actual);
    }

    @Test
    public void errorMessageWorks() {
        String expected = "Variable name is invalid. ";
        assertEquals(expected, validator.validateAssignment(new String[] {"testVar", "1337"}));
    }

}
