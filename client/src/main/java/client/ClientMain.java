package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        String url = "http://localhost:8080";
        if (args.length == 1) {
            url = args[0];
        }
        try {
            new ChessClient();
        }
        catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
    }

    }
}
