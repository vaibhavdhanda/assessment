package com.kraken.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Set;


@ClientEndpoint
public class WebsocketClient {
    private static final Logger LOGGER = LogManager.getLogger(WebsocketClient.class);
    Session userSession = null;
    private MessageHandler messageHandler;


    public WebsocketClient(String apiUrl) {
        LOGGER.info("WebsocketClient: connecting to URL=" + apiUrl);
        try {
            URI apiUrI = new URI(apiUrl);
            WebSocketContainer socketContainer = ContainerProvider.getWebSocketContainer();
            socketContainer.connectToServer(this, apiUrI);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("subscribeTicker ticker = " + e);
            Assert.assertTrue(false, "Connecting to api failed. Please checl the url provide in TestNg suite XML file.");
        }
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

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void logOut() {
        Set<Session> sessions = this.userSession.getOpenSessions();
        sessions.forEach(s -> {
            try {
                s.close();
            } catch (IOException e) {
                LOGGER.error("logOut: Exception when attempting log out - " + e);
                e.printStackTrace();
            }
        });
    }

}