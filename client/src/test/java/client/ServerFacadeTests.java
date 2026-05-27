package client;

import exception.ResponseException;
import model.chessrecords.RegisterResult;
import model.chessrecords.UserData;
import org.junit.jupiter.api.*;
import server.AlreadyTakenException;
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

    @BeforeEach
    public void clear(){
        sFacade.clear();
    }


    @Test
    public void registerPositive() {
        UserData user = new UserData("username", "password", "email");
        RegisterResult result = sFacade.register(user);
        Assertions.assertEquals(user.username(), result.username());
    }

    @Test
    public void registerNegative(){
        UserData user = new UserData("username", "password", "email");
        sFacade.register(user);
        Assertions.assertThrows(ResponseException.class, ()-> sFacade.register(user));

    }

}
