package com.ghpark.hotalk.config;

import com.ghpark.hotalk.model.ChatMessage;
import com.ghpark.hotalk.model.ChatMessageType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.logging.Logger;

@Component
@Log4j2
public class WebSocketEventListener {
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        log.info("새로운 웹 소켓 연결");
    }

    @EventListener
    public void handleWebSocketChatListener(SessionSubscribeEvent event){
        log.info("새로운 구독 " + event.toString());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) stompHeaderAccessor.getSessionAttributes().get("username");
        if(username != null){
            log.info("연결 종료 : " + username);
            ChatMessage message = new ChatMessage();
            message.setType(ChatMessageType.LEAVE);
            message.setSender(username);
            simpMessageSendingOperations.convertAndSend("/topic/public", message);
        }
    }
}

