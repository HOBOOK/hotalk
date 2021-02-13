package com.ghpark.hotalk.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatMessage {
    private String id;
    private ChatMessageType type;
    private String content;
    private String sender;
    private LocalDateTime createDate;
    private LocalDateTime deleteDate;

    public ChatMessage() {
        this.id = UUID.randomUUID().toString();
        this.createDate = LocalDateTime.now();
    }
}
