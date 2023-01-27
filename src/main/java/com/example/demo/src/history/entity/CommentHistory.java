package com.example.demo.src.history.entity;

import com.example.demo.common.Constant.CommentState;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "COMMENT_HISTORY")
public class CommentHistory extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feedId")
    private Feed feed;

    @Column
    @Enumerated(EnumType.STRING)
    private CommentState commentState = CommentState.ACTIVE;

    @Builder
    public CommentHistory(Long id, Long commentId, String content, User user, Feed feed, CommentState commentState){
        this.id = id;
        this.commentId = commentId;
        this.content = content;
        this.user = user;
        this.feed = feed;
        this.commentState = commentState;
    }
}
