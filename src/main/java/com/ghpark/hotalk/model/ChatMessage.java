package com.ghpark.hotalk.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessage {
    private String id;
    private ChatMessageType chatMessageType;
    private String content;
    private String sender;
    private LocalDateTime createDate;
    private LocalDateTime deleteDate;

    public ChatMessage() {
        this.createDate = LocalDateTime.now();
    }
}
