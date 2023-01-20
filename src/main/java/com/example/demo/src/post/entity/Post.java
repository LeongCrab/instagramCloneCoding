package com.example.demo.src.post.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "POST")
public class Post extends BaseEntity {
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
    public Post(Long id, String content, boolean hasImage, boolean hasVideo, int numberOfFiles, User user) {
        this.id = id;
        this.content = content;
        this.hasImage = hasImage;
        this.hasVideo = hasVideo;
        this.numberOfFiles = numberOfFiles;
        this.user = user;
    }
}
