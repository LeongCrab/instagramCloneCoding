package com.example.demo.src.feed.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"id", "loginId", "createdAt", "updatedAt","content"})
public class GetCommentRes {
    private Long Id;
    private String loginId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content;
}