package server;
import org.eclipse.jetty.websocket.api.Session;

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
        }
        else {
            list = connections.get(gameID);
            list.add(session);
        }
        connections.replace(gameID, list);
    }

    public void remove(Session session, int gameID){
        connections.get(gameID).remove(session);
    }

    public void broadcast(Session excludeSession, Notification notification) throws IOException {
        String message = notification.toString();
        for (ArrayList<Session> f : connections.values()){
            for(Session g : f){
                if (g.isOpen()){
                    if(!g.equals(excludeSession)){
                        g.getRemote().sendString(message);
                    }
                }
            }
        }
    }

}
