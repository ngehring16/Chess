package websocket;

import websocket.messages.ServerMessage;

public interface NotificationManager {
    void notify(ServerMessage notification);
}
