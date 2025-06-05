package com.example.demo;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String senderId = (String) session.getAttributes().get("username");

        if (payload.startsWith("username:")) {
            String username = payload.substring(9).trim();
            session.getAttributes().put("username", username);
            sessions.put(username, session);
            broadcast(username + " has joined the chat");
        } else if (payload.startsWith("@")) {
            int space = payload.indexOf(" ");
            if (space != -1) {
                String recipient = payload.substring(1, space);
                String privateMsg = payload.substring(space + 1);
                sendPrivateMessage(senderId, recipient, privateMsg);
            }
        } else {
            broadcast(senderId + ": " + payload);
        }
    }

    private void broadcast(String message) throws Exception {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }

    private void sendPrivateMessage(String sender, String recipient, String message) throws Exception {
        WebSocketSession session = sessions.get(recipient);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage("[Private] " + sender + ": " + message));
        } else {
            WebSocketSession senderSession = sessions.get(sender);
            if (senderSession != null && senderSession.isOpen()) {
                senderSession.sendMessage(new TextMessage("User '" + recipient + "' not found."));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String username = (String) session.getAttributes().get("username");
        sessions.remove(username);
        try {
            broadcast(username + " has left the chat");
        } catch (Exception ignored) {
        }
    }
}
