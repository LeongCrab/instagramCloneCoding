package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.Chat;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class GetChatRes {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String text;
    private String createdAt;

    @Builder
    public GetChatRes(Chat chat){
        this.id = chat.getId();
        this.senderId = chat.getSender().getId();
        this.receiverId = chat.getReceiver().getId();
        this.text = chat.getText();
        this.createdAt = chat.getCreatedAt().toString();
    }
}
