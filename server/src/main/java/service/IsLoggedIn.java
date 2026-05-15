package service;

import chess.ChessGame;
import chess.ChessPiece;
import chessrecords.*;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;

import java.util.ArrayList;

public class IsLoggedIn {

    private final AuthDataAccess authAccess;
    private final UserDataAccess userAccess;
    private final GameDataAccess gameAccess;

    public IsLoggedIn(AuthDataAccess authAccess, UserDataAccess userAccess, GameDataAccess gameAccess){
        this.authAccess = authAccess;
        this.userAccess = userAccess;
        this.gameAccess = gameAccess;
    }

    public void isAuthorized(AuthData auth) throws DoesNotExistException{
        if (auth == null) {
            throw new DoesNotExistException("This authToken does not exist");
        }
    }
    public ChessGame.TeamColor ValidColors(String oldColor, ChessGame.TeamColor team) throws DataAccessException{
        String color = oldColor.toLowerCase();
        if (color.equals("white")){
            team = ChessGame.TeamColor.WHITE;
        }
        else if (color.equals("black")){
            team = ChessGame.TeamColor.BLACK;
        }
        else{
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

    public CreateResult create(String request, String gameName) throws DoesNotExistException, DataAccessException{
        if (gameName == null || gameName.isEmpty() || gameName.equals(" ")){
            throw new DataAccessException("This request is invalid");
        }
        AuthData auth = authAccess.getAuth(request);
        isAuthorized(auth);
        GameData game = gameAccess.createGame(gameName);
        return new CreateResult(game.gameID());

    }

    public void joinGame(JoinRequest request, String authToken) throws DoesNotExistException, DataAccessException, AlreadyTakenException{
        ChessGame.TeamColor team = null;
        team = ValidColors(request.playercolor(), team);
        AuthData auth = authAccess.getAuth(authToken);
        isAuthorized(auth);
        UserData user = userAccess.getUser(auth.username());
        GameData game = gameAccess.getGame(request.gameID());
        if (game == null) {
            throw new DoesNotExistException("This game does not exist");
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




