package client;

import chess.ChessGame;
import exception.ResponseException;
import model.chessrecords.*;

import java.util.ArrayList;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class PostLogin extends LoopTools{
    private final ServerFacade server;
    private final RegisterResult authData;
    private final String authToken;
    private final String url;
    public PostLogin(ServerFacade server, RegisterResult authData, String url){
        this.server = server;
        this.authData = authData;
        this.url = url;
        authToken = authData.authToken();
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
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println("Goodbye! " + authData.username());
        System.out.print(RESET_TEXT_COLOR);
        return "quit";
    }

    public String createGame(){
        String gameName = null;
        System.out.println("What would you like to call your game?");
        while(gameName == null) {
            gameName = getSingleInput("Name: ");
        }
        CreateRequest request = new CreateRequest(gameName);
        server.create(request, authToken);
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println("\nCONGRATS! " + gameName + " was created!");
        System.out.print(RESET_TEXT_COLOR);
        return "";
    }

    public String listGames(){
        ListResult result = server.list(authToken);
        ArrayList<GameData> games = result.games();
        if (games.isEmpty()){
            errorFormat("ERROR: It appears there are currently no games! Please create a new game.");
            return "";
        }
        int j = 0;
        for (int i = 1; i <= games.size(); i ++){
            System.out.println(i + ". " + games.get(j).gameName() +": "
                    + games.get(j).whiteUsername() + ", " + games.get(j).blackUsername() + ", "
                    + SET_TEXT_COLOR_GREEN + games.get(j).game().getGameState().toString() + RESET_TEXT_COLOR);
            j++;
        }
        return "";
    }

    public String playGame(){
        ListResult result = server.list(authToken);
        ArrayList<GameData> games = result.games();
        ChessGame.TeamColor teamColor = null;
        GameData gameData = null;
        String color = "";
        int i = 0;
        if (games.isEmpty()){
            errorFormat("ERROR: It appears there are no games to play! Please create a game in order to play.");
            return "";
        }
        if (!hasOpenGames(games)){
            errorFormat("ERROR: There are no open games to play at this time. Please create a new game in order to play.");
            return "";
        }
        System.out.println("Which game would you like to play?");
        gameData = getGameNumber(i, gameData, games);
        while (gameData.blackUsername() != null && gameData.whiteUsername() != null){
            errorFormat("ERROR: This game is full. Please choose a different game.");
            gameData = getGameNumber(0, gameData, games);
        }
        System.out.println("Which color would you like to play as?");
        teamColor = getTeamColor(teamColor, color, gameData);
        Gameplay gameplay = new Gameplay(gameData, teamColor, url, authToken);
        gameplay.run();
        return "";
    }

    public String observeGame(){
        ListResult result = server.list(authToken);
        ArrayList<GameData> games = result.games();
        ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
        GameData gameData = null;
        int i = 0;
        System.out.println("Which game would you like to watch?");
        gameData = getGameNumber(i, gameData, games);
        Gameplay gameplay = new Gameplay(gameData, teamColor, url, authToken);
        gameplay.run();
        return "";
    }

    private boolean hasOpenGames(ArrayList<GameData> games){
        for (GameData game : games) {
            if (game.whiteUsername() == null || game.blackUsername() == null) {
                return true;
            }
        }
        return false;
    }

    private GameData getGameNumber(int i, GameData gameData, ArrayList<GameData> games){
        while (i < 1 || i > games.size()) {
            String game = getSingleInput("Game number: ");
            if (game == null){
                continue;
            }
            try{
                i = Integer.parseInt(game);
            } catch (NumberFormatException e) {
                errorFormat("ERROR: Please input a number.");
                continue;
            }
            if (i < 1 || i > games.size()){
                errorFormat("ERROR: There is no game with that number! Please enter a valid game number.");
                continue;
            }
            gameData = games.get(i-1);
        }
        return gameData;
    }

    private ChessGame.TeamColor getTeamColor(ChessGame.TeamColor teamColor, String color, GameData gameData){
        while (teamColor == null) {
            color = getSingleInput("BLACK or WHITE?: ");
            if (color == null){
                continue;
            }
            String[] words = color.toLowerCase().split(" ");
            String command = words[0];
            switch (command) {
                case "white" -> teamColor = ChessGame.TeamColor.WHITE;
                case "black" -> teamColor = ChessGame.TeamColor.BLACK;
                default -> {
                    System.out.println("ERROR: Please input a valid color.");
                    continue;
                }
            }
            try{
                server.join(new JoinRequest(teamColor, gameData.gameID()), authToken);
            }
            catch (ResponseException rE){
                errorFormat("ERROR: This color is already taken! Please pick a different one.");
                teamColor = null;
            }
        }
        return teamColor;
    }
}
