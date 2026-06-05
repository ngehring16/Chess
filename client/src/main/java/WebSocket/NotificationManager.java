package WebSocket;

import websocket.messages.ServerMessage;

import javax.management.Notification;

public interface NotificationManager {
    void notify(ServerMessage notification);
}
