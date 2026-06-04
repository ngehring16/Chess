package websocket.messages;

import chess.ChessGame;
import model.chessrecords.ErrorMessage;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    private final ChessGame game;
    private final String message;
    private final String errorMessage;
    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
        this.game = null;
        this.message = null;
        this.errorMessage = null;
    }
    public ServerMessage(ChessGame game){
        this.serverMessageType = ServerMessageType.LOAD_GAME;
        this.game = game;
        this.message = null;
        this.errorMessage = null;
    }
    public ServerMessage(ServerMessageType type, String message){
        this.serverMessageType = type;
        this.game = null;
        this.message = message;
        this.errorMessage = message;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }
    public ChessGame getGame(){
        return game;
    }
    public String getMessage(){
        return message;
    }
    public String getErrorMessage(){
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
