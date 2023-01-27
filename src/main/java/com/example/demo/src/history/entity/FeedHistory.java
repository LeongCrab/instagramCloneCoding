package com.example.demo.src.history.entity;

import com.example.demo.common.Constant.FeedState;
import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "FEED_HISTORY")
public class FeedHistory extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long feedId;
    @Column
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private FeedState feedState = FeedState.ACTIVE;

    @Builder
    public FeedHistory(Long id, Long feedId, String content, User user, FeedState feedState) {
        this.id = id;
        this.feedId = feedId;
        this.content = content;
        this.user = user;
        this.feedState = feedState;
    }
}