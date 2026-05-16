package service;

import chess.ChessGame;
import model.chessrecords.*;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;

public class IsLoggedIn {

    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;
    private final GameDataAccess gameAccess;

    public IsLoggedIn(AuthDataAccess authAccess, UserDataAccess userAccess, GameDataAccess gameAccess){
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }

    private void isAuthorized(AuthData auth) throws DoesNotExistException{
        if (auth == null) {
            throw new DoesNotExistException("This authToken does not exist");
        }
    }
    private ChessGame.TeamColor ValidColors(ChessGame.TeamColor team) throws DataAccessException{
        if (team == null){
            throw new DataAccessException("This color is invalid");
        }
        return team;
    }
    public void logout(String request) throws DoesNotExistException {
        AuthData auth = authAccess.getAuth(request);
        isAuthorized(auth);
        authAccess.deleteAuth(auth);
    }

    public ListResult list(String request) throws DoesNotExistException{
        AuthData auth = authAccess.getAuth(request);
        isAuthorized(auth);
        return new ListResult(gameAccess.listGames());
    }

    public CreateResult create(String authToken, CreateRequest request) throws DoesNotExistException, DataAccessException{
        if (request.gameName() == null || request.gameName().isEmpty() || request.gameName().equals(" ")){
            throw new DataAccessException("This request is invalid");
        }
        AuthData auth = authAccess.getAuth(authToken);
        isAuthorized(auth);
        GameData game = gameAccess.createGame(request.gameName());
        return new CreateResult(game.gameID());

    }

    public void joinGame(JoinRequest request, String authToken) throws DoesNotExistException, DataAccessException, AlreadyTakenException{
        ChessGame.TeamColor team = ValidColors(request.playerColor());
        if (request.gameID() <= 0){
            throw new DataAccessException("This gameID is invalid");
        }
        AuthData auth = authAccess.getAuth(authToken);
        isAuthorized(auth);
        UserData user = userAccess.getUser(auth.username());
        GameData game = gameAccess.getGame(request.gameID());
        if (game == null) {
            throw new DataAccessException("This game does not exist");
        }
        if (team == ChessGame.TeamColor.WHITE && game.whiteUsername() == null){
            gameAccess.updategame(game.gameID(), user.username(), game.blackUsername(), game.gameName(), game.game());
        }
        else if (team == ChessGame.TeamColor.BLACK && game.blackUsername() == null){
            gameAccess.updategame(game.gameID(), game.whiteUsername(), user.username(), game.gameName(), game.game());
        }
        else{
            throw new AlreadyTakenException("This color is already taken");
        }
    }
    }




