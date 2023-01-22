package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.feed.entity.Feed;
import lombok.*;

import javax.persistence.*;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.entity.BaseEntity.State.INACTIVE;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "HEART")
public class Follow extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="followerId")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="followingId")
    private User following;

    @Builder
    public Follow(long id, User follower, User following){
        this.id = id;
        this.follower = follower;
        this.following = following;
    }

    public void toggle() {
        this.state = (this.state == INACTIVE) ? ACTIVE : INACTIVE;
    }
}
