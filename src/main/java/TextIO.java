import java.util.Scanner;

public class TextIO implements IO {
    private Scanner scanner;
    
    public TextIO() {
        scanner = new Scanner(System.in);
    }
    
    @Override
    public String readLine() {
        return scanner.nextLine().replaceAll("\\s", "");
    }
    
    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
