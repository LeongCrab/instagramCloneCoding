package com.example.demo.src.feed.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false)
    private Boolean hasImage;

    @Column(nullable = false)
    private Boolean hasVideo;

    @Column(nullable = false)
    private Integer numberOfFiles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @Builder
    public Feed(Long id, String content, boolean hasImage, boolean hasVideo, int numberOfFiles, User user) {
        this.id = id;
        this.content = content;
        this.hasImage = hasImage;
        this.hasVideo = hasVideo;
        this.numberOfFiles = numberOfFiles;
        this.user = user;
    }

    public void modifyFeed(String content) {
        this.content = content;
    }

    public void deleteFeed() {
        this.state = State.INACTIVE;
    }

    public void banFeed() {
        this.state = State.BANNED;
    }
}
