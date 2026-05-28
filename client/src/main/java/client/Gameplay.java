package client;

import exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

public class Gameplay {
    private final ServerFacade server;
    public Gameplay(ServerFacade server){
        this.server = server;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            System.out.print(help());
            String line = scanner.nextLine();

            try {
                result = eval(line);

            }
            catch(Throwable ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public String eval(String input){
        try {
            String[] words = input.toLowerCase().split(" ");
            String command = (words.length > 0) ? words[0] : "help";
            return switch (command) {
                case "quit" -> "quit";
                case "display"-> display();
                default -> help();
            };
        }
        catch(ResponseException ex){
            throw new ResponseException(ex.getMessage());
        }

    }

    public String help(){
        return """
                
                GAMEPLAY:
                -quit Game
                -display
                """;
    }

    public String display(){
        return "here's the board!";
    }
}
