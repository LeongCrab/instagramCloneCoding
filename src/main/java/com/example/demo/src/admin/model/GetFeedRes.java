package com.example.demo.src.admin.model;


import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFeedRes {
    private Long id;
    private String loginId;
    private String createAt;
    public GetFeedRes(Feed feed) {
        this.id = feed.getId();
        this.loginId = feed.getUser().getLoginId();
        this.createAt = feed.getCreatedAt().toString();
    }
}