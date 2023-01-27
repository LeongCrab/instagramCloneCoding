package com.example.demo.src.feed.entity;

import com.example.demo.common.Constant.FeedState;
import com.example.demo.common.Constant.State;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "FEED")
public class Feed extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private FeedState feedState = FeedState.ACTIVE;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Video> videoList = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Feed(Long id, String content, User user) {
        this.id = id;
        this.content = content;
        this.user = user;
    }

    public void modifyFeed(String content) {
        this.content = content;
    }
    public void hideFeed() {
        this.state = State.INACTIVE;
        this.feedState = FeedState.INVISIBLE;
    }
    public void deleteFeed() {
        this.state = State.INACTIVE;
        this.feedState = FeedState.DELETED;
    }

    public void banFeed() {
        this.state = State.INACTIVE;
        this.feedState = FeedState.BANNED;
    }
}
