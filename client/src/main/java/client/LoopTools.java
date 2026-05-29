package client;

import java.util.Scanner;

public class LoopTools {
    public LoopTools(){}

    public String getSingleInput(String format){
        System.out.print(format);
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (line.isBlank()){
            System.out.println("Please give a valid input.");
            return null;
        }
        var words = line.toLowerCase().split(" ");
        return words[0];
    }
}
