package client;

import chess.ChessGame;
import exception.ResponseException;
import model.chessrecords.*;
import org.junit.jupiter.api.*;
import server.AlreadyTakenException;
import server.Server;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade sFacade;
    private final UserData user = new UserData("username", "password", "email");
    private final LoginRequest login = new LoginRequest("username", "password");

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
    public void setup(){
        sFacade.clear();
    }


    @Test
    public void registerPositive() {
        RegisterResult result = sFacade.register(user);
        Assertions.assertEquals(user.username(), result.username());
    }

    @Test
    public void registerNegative(){
        sFacade.register(user);
        Assertions.assertThrows(ResponseException.class, ()-> sFacade.register(user));

    }

    @Test
    public void loginPositive() {
        RegisterResult register = sFacade.register(user);
        RegisterResult result = sFacade.login(login);
        Assertions.assertEquals(register.username(), result.username());

    }

    @Test
    public void loginNegative() {
        Assertions.assertThrows(ResponseException.class, ()-> sFacade.login(login));
    }

    @Test
    public void logoutPositive() {
        RegisterResult result = sFacade.register(user);
        sFacade.logout(result.authToken());
        Assertions.assertThrows(ResponseException.class, ()->sFacade.list(result.authToken()));
    }

    @Test
    public void logoutNegative() {
        Assertions.assertThrows(ResponseException.class, ()->sFacade.logout("fakeToken"));
    }

    @Test
    public void listPositive() {
        RegisterResult result = sFacade.register(user);
        sFacade.create(new CreateRequest("game1"), result.authToken());
        ListResult listResult = sFacade.list(result.authToken());
        Assertions.assertEquals(1, listResult.games().size());
    }

    @Test
    public void listNegative() {
        Assertions.assertThrows(ResponseException.class, ()->sFacade.list("fakeToken"));
    }

    @Test
    public void createPositive() {
        RegisterResult result = sFacade.register(user);
        CreateResult createResult = sFacade.create(new CreateRequest("game1"), result.authToken());
        Assertions.assertEquals(10, createResult.gameID() );
    }

    @Test
    public void createNegative() {
        RegisterResult result = sFacade.register(user);
        sFacade.logout(result.authToken());
        Assertions.assertThrows(ResponseException.class,
                ()->sFacade.create(new CreateRequest("game1"), result.authToken()));
    }

    @Test
    public void joinPositive() {
        RegisterResult result = sFacade.register(user);
        CreateResult createResult = sFacade.create(new CreateRequest("game1"), result.authToken());
        sFacade.join(new JoinRequest(ChessGame.TeamColor.WHITE, createResult.gameID()), result.authToken());
        ArrayList<GameData> list = sFacade.list(result.authToken()).games();
        Assertions.assertEquals(list.getFirst().whiteUsername(), result.username());
    }

    @Test
    public void joinNegative() {
        RegisterResult result = sFacade.register(user);
        Assertions.assertThrows(ResponseException.class,
                ()->sFacade.join(new JoinRequest(ChessGame.TeamColor.BLACK, 10), result.authToken()));
    }

    @Test
    public void clear() {
        RegisterResult result = sFacade.register(user);
        CreateResult createResult = sFacade.create(new CreateRequest("game1"), result.authToken());
        sFacade.clear();
        Assertions.assertThrows(ResponseException.class, ()->
                sFacade.join(new JoinRequest(ChessGame.TeamColor.BLACK, createResult.gameID()), result.authToken()));
    }

}
