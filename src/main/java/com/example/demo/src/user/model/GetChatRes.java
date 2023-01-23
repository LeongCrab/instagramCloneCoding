package com.example.demo.src.user.model;

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
    public GetChatRes(Long id, Long senderId, Long receiverId, String text, LocalDateTime createdAt){
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.createdAt = createdAt.toString();
    }
}
