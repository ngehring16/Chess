package WebSocket;

import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.messages.ServerMessage;

import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationManager notificationManager;
    public WebSocketFacade(String url, NotificationManager notificationManager) throws ResponseException {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationManager = notificationManager;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    notificationManager.notify(serverMessage);
                }
            });
        } catch (Exception e) {
            notificationManager.notify(new ServerMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
        }
    }





    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
