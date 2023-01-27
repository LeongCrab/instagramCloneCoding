package com.example.demo.src.history;


import com.example.demo.src.feed.entity.Comment;
import com.example.demo.src.history.entity.CommentHistory;
import com.example.demo.utils.BeanUtil;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class CommentEntityListener {
    @PostPersist
    @PostUpdate
    public void postPersistAndPostUpdate(Object o) {
        CommentHistoryRepository commentHistoryRepository = BeanUtil.getBean(CommentHistoryRepository.class);

        Comment comment = (Comment) o;

        CommentHistory commentHistory = CommentHistory.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .user(comment.getUser())
                .feed(comment.getFeed())
                .commentState(comment.getCommentState())
                .build();

        commentHistoryRepository.save(commentHistory);
    }
}
