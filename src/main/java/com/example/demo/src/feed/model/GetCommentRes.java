package com.example.demo.src.feed.model;

import com.example.demo.src.feed.entity.Comment;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"id", "loginId", "content", "createdAt", "updatedAt", "state"})
public class GetCommentRes {
    private Long id;
    private String loginId;
    private String content;
    private String createdAt;
    private String updatedAt;
    private String state;
    public GetCommentRes(Comment comment){
        this.id = comment.getId();
        this.loginId = comment.getUser().getLoginId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt().toString();
        this.updatedAt = comment.getUpdatedAt().toString();
        this.state = comment.getState().toString();

    }
}