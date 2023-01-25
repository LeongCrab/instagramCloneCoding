package com.example.demo.src.feed.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

import static com.example.demo.common.Constant.State;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "HEART")
public class Heart extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feedId")
    private Feed feed;

    @Builder
    public Heart(long id, User user, Feed feed){
        this.id = id;
        this.user = user;
        this.feed = feed;
    }

    public void toggle() {
        this.state = (this.state == State.INACTIVE) ? State.ACTIVE : State.INACTIVE;
    }
}
