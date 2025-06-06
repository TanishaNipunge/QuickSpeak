package com.example.demo.service;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConnectedUserService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Set<String> connectedUsers = ConcurrentHashMap.newKeySet();

    public ConnectedUserService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void addUser(String username) {
        connectedUsers.add(username);
        sendUserListUpdate();
    }

    public void removeUser(String username) {
        connectedUsers.remove(username);
        sendUserListUpdate();
    }

    public Set<String> getAllUsers() {
        return connectedUsers;
    }

    private void sendUserListUpdate() {
        messagingTemplate.convertAndSend("/topic/users", connectedUsers);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            removeUser(username);
        }
    }
}
