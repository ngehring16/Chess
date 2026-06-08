package client;

import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public abstract class LoopTools {
    public LoopTools(){}

    public String getSingleInput(String format){
        System.out.print(format);
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (line.isBlank()){
            errorFormat("ERROR: Please give a valid input.");
            return null;
        }
        var words = line.toLowerCase().split(" ");
        return words[0];
    }

    public void runLoop(String result,String help, Scanner scanner){
        while (!result.equals("quit")){
            System.out.print(help);
            String line = scanner.nextLine();
            if (line.isBlank()){
                errorFormat("ERROR: Please enter a valid input.");
                continue;
            }
            try {
                result = eval(line);

            }
            catch(Throwable ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public void errorFormat(String message){
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println(message);
        System.out.print(RESET_TEXT_COLOR);
    }

    public abstract String eval(String line);
}
