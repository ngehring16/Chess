package client;

import exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

public class Gameplay extends LoopTools{
    private final ServerFacade server;
    public Gameplay(ServerFacade server){
        this.server = server;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        var result = "";
        runLoop(result, help(), scanner);
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
