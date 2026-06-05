package server;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public final HashMap<Integer, ArrayList<Session>> connections = new HashMap<>();

    public void add(Session session, Integer gameID){
        ArrayList<Session> list = new ArrayList<>();
        if (connections.get(gameID) == null){
            list.add(session);
            connections.put(gameID, list);
        }
        else {
            list = connections.get(gameID);
            list.add(session);
            connections.replace(gameID, list);
        }
    }

    public void remove(Session session, int gameID){
        connections.get(gameID).remove(session);
    }

    public void broadcast(Session excludeSession, ServerMessage notification, Integer gameID) throws IOException {
        String message = new Gson().toJson(notification);
        for (ArrayList<Session> f : connections.values()){
            if (connections.get(gameID) != f){
                continue;
            }
            for(Session g : f){
                if (!g.equals(excludeSession)){
                    if(g.isOpen()){
                        g.getRemote().sendString(message);
                    }
                }
            }
        }
    }

}
