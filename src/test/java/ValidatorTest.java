import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ValidatorTest {
    Validator validator = new Validator();

    @Test
    public void validAssignmentPasses() {
        String[] parts = new String[] { "x", "1337" };
        String expected = "";
        String actual = validator.validateAssignment(parts);
        assertEquals(expected, actual);
    }
}
