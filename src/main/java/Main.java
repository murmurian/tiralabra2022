public class Main {
    public static void main(String[] args) {
        IO io = new TextIO();
        Calculator calculator = new Calculator();

        printInstructions(io);

        while (true) {
            io.print("Enter an expression or assign a variable (use \"help\" to print instructions):");
            String expression = io.readLine();
            if (expression.equals("quit"))
                break;
            io.print(calculator.calculate(expression));
        }
    }

    private static void printInstructions(IO io) {
        io.print("Welcome to the calculator!\n");
        io.print("This calculator supports the following operations: +, -, *, /, ^, sqrt, sin, cos, tan");
        io.print("You may assign a variable by using the following syntax: x = 5");
        io.print("You may also assign a variable to the result of an expression: x = 5 + 5");
        io.print("Supported variables consist of 1-3 letters followed by optional 1-4 digits.");
        io.print("For example: x, y, x1, y2, xyz, xy123 etc.\n");
        io.print("You can quit the program by entering quit");
        io.print("Type \"help\" to print these instructions again.\n");
    }
}
