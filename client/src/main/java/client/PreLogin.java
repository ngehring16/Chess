package client;

import exception.ResponseException;
import model.chessrecords.LoginRequest;
import model.chessrecords.RegisterResult;
import model.chessrecords.UserData;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreLogin {
    private final ServerFacade server;
    private final LoopTools loopTools = new LoopTools();
    public PreLogin(ServerFacade server){
        this.server = server;
    }

    public void run(){
        System.out.println(SET_TEXT_BOLD + "♔ WELCOME CHESS CHAMPION. SIGN IN TO START ♚");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            System.out.print(help());
            String line = scanner.nextLine();
            if (line.isBlank()){
                System.out.println("Please enter a valid input.");
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
            username = loopTools.getSingleInput("Input username: ");
        }
        while(password == null){
            password = loopTools.getSingleInput("Input password: ");
        }
        LoginRequest loginRequest = new LoginRequest(username, password);
        RegisterResult result = server.login(loginRequest);
        System.out.println("Successfully Logged in as " + result.username());
        PostLogin postLogin = new PostLogin(server, result);
        postLogin.run();
        return "";
    }

    public String register(){
        String username = null;
        String password = null;
        String email = null;
        System.out.println("Enter your information below:");
        while (username == null) {
            username = loopTools.getSingleInput("Input username: ");
        }
        while(password == null){
            password = loopTools.getSingleInput("Input password: ");
        }
        while(email == null){
            email = loopTools.getSingleInput("Input email:");
        }
        UserData user = new UserData(username, password, email);
        RegisterResult result = server.register(user);
        System.out.println("Successfully Logged in as " + result.username());
        PostLogin postLogin = new PostLogin(server, result);
        postLogin.run();
        return "";
    }
}
