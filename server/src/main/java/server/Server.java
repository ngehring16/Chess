package server;

import model.chessrecords.*;
import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;

public class Server {

    private final Javalin javalin;
    private final AuthDataAccess authAccess = new AuthDataAccess();
    private final UserDataAccess userAccess = new UserDataAccess();
    private final GameDataAccess gameAccess = new GameDataAccess();
    private final GettingStarted starter = new GettingStarted(authAccess, userAccess);
    private final IsLoggedIn loggedIn = new IsLoggedIn(authAccess, userAccess, gameAccess);
    private final ClearAll clearer = new ClearAll(userAccess, authAccess, gameAccess);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", this::register);
        javalin.post("/session", this::login );
        javalin.delete("/db", this::clear);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::list);
        javalin.post("/game", this::create);
        javalin.put("/game", this::join);
    }

    public void register(Context cxt){
    try {
        UserData user = new Gson().fromJson(cxt.body(), UserData.class);
        RegisterResult result = starter.register(user);
        cxt.result(new Gson().toJson(result));
    }
    catch (AlreadyTakenException E){
        cxt.result(new Gson().toJson( new ErrorMessage("Error: already taken")));
        cxt.status(403);
    }
    catch (DataAccessException E){
        cxt.result(new Gson().toJson( new ErrorMessage("Error: bad request")));
        cxt.status(400);
    }
    catch(Exception H){
        cxt.result(new Gson().toJson(new ErrorMessage("Error: " + H.getMessage())));
        cxt.status(500);
    }
    }

    public void login(Context cxt){
        try{
            LoginRequest request = new Gson().fromJson(cxt.body(), LoginRequest.class);
            RegisterResult result = starter.login(request);
            cxt.result(new Gson().toJson(result));
        }
        catch (DoesNotExistException | DoesNotMatchException F){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: unauthorized")));
            cxt.status(401);
        }
        catch (DataAccessException G){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: bad request")));
            cxt.status(400);
        }
        catch(Exception H){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: " + H.getMessage())));
            cxt.status(500);
        }
    }

    public void logout(Context cxt){
        try{
            String authToken = cxt.header("authorization");
            loggedIn.logout(authToken);
            cxt.result();
            cxt.status(200);
        }
        catch(DoesNotExistException E){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: unauthorized")));
            cxt.status(401);
        }
        catch(Exception H){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: " + H.getMessage())));
            cxt.status(500);
        }
    }

    public void list(Context cxt){
        try{
            String authToken = cxt.header("authorization");
            ListResult result = loggedIn.list(authToken);
            cxt.result(new Gson().toJson(result));
        }
        catch(DoesNotExistException E){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: unauthorized")));
            cxt.status(401);
        }
        catch(Exception H){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: " + H.getMessage())));
            cxt.status(500);
        }
    }

    public void create(Context cxt){
        try{
            String authToken = cxt.header("authorization");
            CreateRequest gameName = new Gson().fromJson(cxt.body(), CreateRequest.class);
            CreateResult result = loggedIn.create(authToken, gameName);
            cxt.result(new Gson().toJson(result));
        }
        catch (DataAccessException G){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: bad request")));
            cxt.status(400);
        }
        catch(DoesNotExistException E){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: unauthorized")));
            cxt.status(401);
        }
        catch(Exception H){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: " + H.getMessage())));
            cxt.status(500);
        }

    }

    public void join(Context cxt){
        try{
            String authToken = cxt.header("authorization");
            JoinRequest request = new Gson().fromJson(cxt.body(), JoinRequest.class);
            loggedIn.joinGame(request, authToken);
            cxt.result();
            cxt.status(200);
        }
        catch (AlreadyTakenException E){
            cxt.result(new Gson().toJson( new ErrorMessage("Error: already taken")));
            cxt.status(403);
        }
        catch(DoesNotExistException E){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: unauthorized")));
            cxt.status(401);
        }
        catch (DataAccessException G){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: bad request")));
            cxt.status(400);
        }
        catch(Exception H){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: " + H.getMessage())));
            cxt.status(500);
        }
    }

    public void clear(Context cxt){
        try {
            clearer.clear();
            cxt.result();
            cxt.status(200);
        }
        catch(Exception H){
            cxt.result(new Gson().toJson(new ErrorMessage("Error: " + H.getMessage())));
            cxt.status(500);
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
