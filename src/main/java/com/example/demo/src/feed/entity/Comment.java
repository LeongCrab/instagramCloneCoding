package com.example.demo.src.feed.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

import static com.example.demo.common.Constant.State.INACTIVE;

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
    @Size(min=1, max=200, message = "댓글은 1자 이상 200자 이하로 입력 가능합니다.")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feedId")
    private Feed feed;

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
        this.state = INACTIVE;
    }
}
