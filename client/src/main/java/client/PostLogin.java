package client;

import chess.ChessGame;
import exception.ResponseException;
import model.chessrecords.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PostLogin {
    private final ServerFacade server;
    private final RegisterResult authData;
    private final String authToken;
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
        server.create(request, authToken);
        System.out.println("\nCONGRATS! " + gameName + " was created!");
        return "";
    }

    public String listGames(){
        ListResult result = server.list(authToken);
        ArrayList<GameData> games = result.games();
        if (games.isEmpty()){
            System.out.println("It appears there are currently no games! Please create a new game.");
            return "";
        }
        int j = 0;
        for (int i = 1; i <= games.size(); i ++){
            System.out.println(i + ". " + games.get(j).gameName() +": "
                    + games.get(j).whiteUsername() + ", " + games.get(j).blackUsername());
            j++;
        }
        return "";
    }

    public String playGame(){
        ListResult result = server.list(authToken);
        ArrayList<GameData> games = result.games();
        ChessGame.TeamColor teamColor = null;
        String color = "";
        int i = 0;
        if (games.isEmpty()){
            System.out.println("It appears there are no games to play! Please create a game in order to play.");
            return "";
        }
        System.out.println("Which game would you like to play?");
        while (i < 1 || i > games.size()) {
            String game = getSingleInput("Game number: ");
            try{
                i = Integer.parseInt(game);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please input a number.");
                continue;
            }
            if (i < 1 || i > games.size()){
                System.out.println("There is no game with that number! Please enter a valid game number.");
            }
        }
        GameData gameData = games.get(i-1);
        System.out.println("Which color would you like to play as?");
        while (teamColor == null) {
            color = getSingleInput("BLACK or WHITE?: ");
            String[] words = color.toLowerCase().split(" ");
            String command = words[0];
            switch (command) {
                case "white" -> teamColor = ChessGame.TeamColor.WHITE;
                case "black" -> teamColor = ChessGame.TeamColor.BLACK;
                default -> System.out.println("Please input a valid color.");
            }
            try{
                server.join(new JoinRequest(teamColor, gameData.gameID()), authToken);
            }
            catch (ResponseException rE){
                System.out.println("This color is already taken! Please pick a different one.");
                teamColor = null;
            }
        }
        Gameplay gameplay = new Gameplay(server);
        gameplay.run();
        return "";
    }

    public String observeGame(){
        return "";
    }
}
