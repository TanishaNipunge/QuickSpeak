package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.demo.model.ChatMessage;
import com.example.demo.service.ConnectedUserService;

@Controller
public class ChatController {

    private final ConnectedUserService connectedUserService;

    public ChatController(ConnectedUserService connectedUserService) {
        this.connectedUserService = connectedUserService;
    }

    @MessageMapping("/register")
    public void register(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        connectedUserService.addUser(message.getSender());
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
}
