package com.example.demo.src.admin.model;

import com.example.demo.src.feed.entity.Comment;
import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.feed.model.GetCommentRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFeedInfoRes {
    private Long id;
    private String content;
    private String loginId;
    private String createdAt;
    private String updatedAt;
    private String state;
    private List<GetCommentRes> comments = new ArrayList<>();

    public GetFeedInfoRes(Feed feed, List<Comment> commentList) {
        this.id = feed.getId();
        this.content = feed.getContent();
        this.loginId = feed.getUser().getLoginId();
        this.createdAt = feed.getCreatedAt().toString();
        this.updatedAt = feed.getUpdatedAt().toString();
        this.state = feed.getState().toString();
        for(Comment comment: commentList){
            this.comments.add(new GetCommentRes(comment));
        }
    }
}
