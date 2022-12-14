public class Validator {
    private String errorMessage;

    public Validator() {
        errorMessage = "";
    }

    public String validateAssignment(String[] parts) {
        if (parts.length > 2) {
            errorMessage = "You can only assign one variable at a time. ";
        }
        if (!validateVariable(parts[0])) {
            errorMessage += "Variable name is invalid.";
        }
        return this.errorMessage;
    }

    public boolean validateVariable(String string) {
        return string.matches("[a-zA-Z]{1,3}[0-9]{0,4}");
    }
    
}
