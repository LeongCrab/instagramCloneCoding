package com.example.demo.src.feed.entity;

import com.example.demo.common.Constant.State;
import com.example.demo.common.Constant.CommentState;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.history.CommentEntityListener;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@EntityListeners(value = CommentEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "COMMENT")
public class Comment extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    public Comment(long id, String content, User user, Feed feed){
        this.id = id;
        this.content = content;
        this.user = user;
        this.feed = feed;
    }

    public void patchComment(String content) {
        this.content = content;
    }

    public void deleteComment() {
        this.state = State.INACTIVE;
        this.commentState = CommentState.DELETED;
    }

    public void hideComment() {
        this.state = State.INACTIVE;
        this.commentState = CommentState.INVISIBLE;
    }
}
