package com.example.demo.src.history.model;

import com.example.demo.src.history.entity.CommentHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentHistoryRes {
    private Long id;
    private Long commentId;
    private String CreatedAt;

    public GetCommentHistoryRes(CommentHistory commentHistory) {
        this.id = commentHistory.getId();
        this.commentId = commentHistory.getCommentId();
        this.CreatedAt = commentHistory.getCreatedAt().toString();
    }
}