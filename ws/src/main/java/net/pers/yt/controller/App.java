package net.pers.yt.controller;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws/app/{nickName}")
public class App {

	private static final Set<App> connections = new CopyOnWriteArraySet<App>();
    private String nickName;
	
    private Session session;
    
    @OnOpen
    public void onOpen(Session session,
            @PathParam(value = "nickName") String nickName) {

        this.session = session;
        this.nickName = nickName;

        connections.add(this);
        String message = String.format("System> %s %s", this.nickName,
                " has joined.");
        App.broadCast(message);
    }
    
    @OnClose
    public void onClose() {
        connections.remove(this);
        String message = String.format("System> %s, %s", this.nickName,
                " has disconnection.");
        App.broadCast(message);
    }
    
    @OnMessage
    public void onMessage(String message,
            @PathParam(value = "nickName") String nickName) {
    	System.out.println(message);
        App.broadCast("yothin" + ">" + "你好");
        
    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }
    
    private static void broadCast(String message) {
        for (App app : connections) {
            try {
                synchronized (app) {
                    app.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                connections.remove(app);
                try {
                    app.session.close();
                } catch (IOException e1) {
                }
                app.broadCast(String.format("System> %s %s", app.nickName,
                        " has bean disconnection."));
            }
        }
    }
}
