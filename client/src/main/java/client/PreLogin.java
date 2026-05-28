package client;

import exception.ResponseException;
import model.chessrecords.LoginRequest;
import model.chessrecords.RegisterResult;
import model.chessrecords.UserData;

import java.util.Arrays;
import java.util.Scanner;

public class PreLogin {
    private final ServerFacade server;
    public PreLogin(ServerFacade server){
        this.server = server;
    }

    private String getSingleInput(String format){
        System.out.print(format);
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        var words = line.toLowerCase().split(" ");
        return words[0];
    }

    public void run(){
        System.out.println("♔ WELCOME CHESS CHAMPION. SIGN IN TO START ♚");

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
                case "register" -> register();
                case "login" -> login(params);
                case "quit" -> "quit";
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
                - register
                - login
                - quit
                - help
                __________
                
                """;
    }

    public String login(String[] params){
        String username = getSingleInput("Input username: ");
        String password = getSingleInput("Input password: ");
        LoginRequest loginRequest = new LoginRequest(username, password);
        RegisterResult result = server.login(loginRequest);
        System.out.println("Successfully Logged in as " + result.username());
        PostLogin postLogin = new PostLogin(server, result);
        postLogin.run();
        return "";
    }

    public String register(){
        System.out.println("Enter your information below:");
        String username = getSingleInput("Input username: ");
        String password = getSingleInput("Input password: ");
        String email = getSingleInput("Input email:");
        UserData user = new UserData(username, password, email);
        RegisterResult result = server.register(user);
        System.out.println("Successfully Logged in as " + result.username());
        PostLogin postLogin = new PostLogin(server, result);
        postLogin.run();
        return "";
    }
}
