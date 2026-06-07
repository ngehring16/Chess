package client;

import exception.ResponseException;
import model.chessrecords.LoginRequest;
import model.chessrecords.RegisterResult;
import model.chessrecords.UserData;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreLogin extends LoopTools{
    private final ServerFacade server;
    private final String url;
    public PreLogin(ServerFacade server, String url){
        this.server = server;
        this.url = url;
    }

    public void run(){
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println(SET_TEXT_BOLD + "♔ WELCOME CHESS CHAMPION. SIGN IN TO START ♚");
        System.out.print(RESET_TEXT_COLOR);
        Scanner scanner = new Scanner(System.in);
        var result = "";
        runLoop(result, help(), scanner);
    }

    public String eval(String input){
        try {
            String[] words = input.toLowerCase().split(" ");
            String command = (words.length > 0) ? words[0] : "help";
            return switch (command) {
                case "register" -> register();
                case "login" -> login();
                case "quit" -> "quit";
                default -> help();
            };
        }
        catch(ResponseException ex){
            throw new ResponseException(ex.getMessage());
        }
    }

    public String help(){
        System.out.print(SET_TEXT_BOLD);
        return """
                
                ACTIONS:
                - REGISTER
                - LOGIN
                - QUIT
                - HELP
                __________
                
                """;
    }

    public String login(){
        String username = null;
        String password = null;
        while (username == null) {
            username = getSingleInput("Input username: ");
        }
        while(password == null){
            password = getSingleInput("Input password: ");
        }
        LoginRequest loginRequest = new LoginRequest(username, password);
        RegisterResult result = server.login(loginRequest);
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println("Successfully Logged in as " + result.username());
        System.out.print(RESET_TEXT_COLOR);
        PostLogin postLogin = new PostLogin(server, result, url);
        postLogin.run();
        return "";
    }

    public String register(){
        String username = null;
        String password = null;
        String email = null;
        System.out.println("Enter your information below:");
        while (username == null) {
            username = getSingleInput("Input username: ");
        }
        while(password == null){
            password = getSingleInput("Input password: ");
        }
        while(email == null){
            email = getSingleInput("Input email:");
        }
        UserData user = new UserData(username, password, email);
        RegisterResult result = server.register(user);
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println("Successfully Logged in as " + result.username());
        System.out.print(RESET_TEXT_COLOR);
        PostLogin postLogin = new PostLogin(server, result, url);
        postLogin.run();
        return "";
    }
}
