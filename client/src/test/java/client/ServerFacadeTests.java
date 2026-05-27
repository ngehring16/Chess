package client;

import model.chessrecords.RegisterResult;
import model.chessrecords.UserData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade sFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        var url = "http://localhost:" + port;
        System.out.println("Started test HTTP server on " + port);
        sFacade = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void register() {
        UserData user = new UserData("username", "password", "email");
        RegisterResult result = sFacade.register(user);
        Assertions.assertEquals(user.username(), result.username());
    }

}
