public class TestIO implements IO {
    private String input;
    private String output;

    public TestIO(String input) {
        this.input = input;
        output = "";
    }

    @Override
    public String readLine() {
        return input;
    }

    @Override
    public void print(String message) {
        this.output = message;
    }

    @Override
    public String getOutput() {
        return output;
    }
}
