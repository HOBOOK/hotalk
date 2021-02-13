package com.ghpark.hotalk.controller;

import com.ghpark.hotalk.model.ChatMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage message){
        return message;
    }

    @MessageMapping("/chat.sendMessage/{id}")
    @SendTo("/topic/{id}")
    public ChatMessage sendMessageToRoom(@Payload ChatMessage message){
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor accessor){
        accessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

    @MessageMapping("/chat.addUser/{id}")
    @SendTo("/topic/{id}")
    public ChatMessage addUserToRoom(@Payload ChatMessage message, SimpMessageHeaderAccessor accessor){
        accessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }

    @MessageMapping("/chat.removeUser")
    @SendTo("/topic/public")
    public ChatMessage removeUser(@Payload ChatMessage message, SimpMessageHeaderAccessor accessor){
        accessor.getSessionAttributes().remove(message.getSender());
        return message;
    }

    @MessageMapping("/chat.removeUser/{id}")
    @SendTo("/topic/{id}")
    public ChatMessage removeUserToRoom(@Payload ChatMessage message, SimpMessageHeaderAccessor accessor){
        accessor.getSessionAttributes().remove(message.getSender());
        return message;
    }
}
