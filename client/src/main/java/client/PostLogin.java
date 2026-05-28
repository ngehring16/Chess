package client;

import exception.ResponseException;
import model.chessrecords.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PostLogin {
    private final ServerFacade server;
    private final RegisterResult authData;
    private String authToken;
    public PostLogin(ServerFacade server, RegisterResult authData){
        this.server = server;
        this.authData = authData;
        authToken = authData.authToken();
    }

    private String getSingleInput(String format){
        System.out.print(format);
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        var words = line.toLowerCase().split(" ");
        return words[0];
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
            String[] params = Arrays.copyOfRange(words, 1, words.length);
            return switch (command) {
                case "logout" -> logout();
                case "create"-> createGame();
                case "list"-> listGames();
                case "play"-> playGame();
                case "observe"-> observeGame();
                default -> help();
            };
        }
        catch(ResponseException ex){
            throw new ResponseException(ex.getMessage());
        }
    }

    public String help(){
        return """
                
                ACTIONS:             
                - help
                - logout
                - create game
                - list games
                - play game
                - observe game
                __________
                
                """;
    }

    public String logout(){
        server.logout(authToken);
        System.out.println("Goodbye! " + authData.username());
        return "quit";
    }

    public String createGame(){
        System.out.println("What would you like to call your game?");
        String gameName = getSingleInput("Name: ");
        CreateRequest request = new CreateRequest(gameName);
        CreateResult result = server.create(request, authToken);
        System.out.println("\nCONGRATS! " + gameName + " was created!\n" +
                "You can find it with the gameID: " + result.gameID());
        return "";
    }

    public String listGames(){
        ListResult result = server.list(authToken);
        ArrayList<GameData> games = result.games();
        int j = 0;
        for (int i = 1; i <= games.size(); i ++){
            System.out.println(i + ". " + games.get(j).gameName() +": "
                    + games.get(j).whiteUsername() + ", " + games.get(j).blackUsername()
                    + ", GameID#: " + games.get(j).gameID());
            j++;
        }
        return "";
    }

    public String playGame(){
        return "";
    }

    public String observeGame(){
        return "";
    }
}
