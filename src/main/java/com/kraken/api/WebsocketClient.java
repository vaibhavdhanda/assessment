package com.kraken.api;

import java.io.IOException;
import java.net.URI;
import java.util.Set;
import javax.websocket.*;


@ClientEndpoint
public class WebsocketClient {

    Session userSession = null;
    private MessageHandler messageHandler;

    public WebsocketClient(URI uri) throws Exception{
        WebSocketContainer socketContainer = ContainerProvider.getWebSocketContainer();
        socketContainer.connectToServer(this, uri);
        this.messageHandler = new MessageHandler();
    }

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public MessageHandler getMessageHandler(){
        return messageHandler;
    }

    public void logOut(){
        Set<Session> sessions = this.userSession.getOpenSessions();
        sessions.forEach( s -> {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}