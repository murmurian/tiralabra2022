public class Main {
    public static void main(String[] args) {
        IO io = new TextIO();
        Calculator calculator = new Calculator(true);
        String expression = "";

        printInstructions(io);

        while (true) {
            io.print("Enter an expression or assign a variable (use \"help\" to print instructions):");
            expression = io.readLine();
            if (expression.equals("quit"))
                break;
            switch (expression) {
                case "help" -> {
                    printInstructions(io);
                    continue;
                }
                case "deg" -> {
                    calculator.setAngle("deg", io);
                    continue;
                }
                case "rad" -> {
                    calculator.setAngle("rad", io);
                    continue;
                }
            }

            io.print(calculator.calculate(expression, io));
        }
    }

    private static void printInstructions(IO io) {
        io.print("\nWelcome to the calculator!\n");
        io.print("This calculator supports the following operations: +, -, *, /, ^, sqrt\nLogarithms: log, ln");
        io.print("Trigonometric functions: sin, cos, tan");
        io.print("Change between degrees and radians by entering deg or rad\nDefault angle mode is radians\n");
        io.print("You may assign a variable by using the following syntax: x = 5");
        io.print("You may also assign a variable to the result of an expression: x = 5 + 5");
        io.print("Supported variables consist of 1-3 letters followed by optional 1-4 digits.");
        io.print("For example: x, y, x1, y2, xyz, xy123 etc.\n");
        io.print("pi and e are preset variables, you may change them at your own risk \n");
        io.print("You can quit the program by entering quit");
        io.print("Type \"help\" to print these instructions again.\n");
    }
}
