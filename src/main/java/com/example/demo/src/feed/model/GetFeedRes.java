package com.example.demo.src.feed.model;

import com.example.demo.src.feed.entity.Feed;

import com.example.demo.src.feed.entity.Image;
import com.example.demo.src.feed.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
public class GetFeedRes {
    private Long id;
    private Long userId;
    private String createdAt;
    private String updatedAt;
    private String content;
    private Boolean hasImage;
    private Boolean hasVideo;
    private Integer numberOfContents;
    private Integer hearts;
    private Integer comments;
    private List<String> imageList;
    private List<String> videoList;

    public GetFeedRes(Feed feed, int hearts) {
        this.id = feed.getId();
        this.userId = feed.getUser().getId();
        this.createdAt = feed.getCreatedAt().toString();
        this.updatedAt = feed.getUpdatedAt().toString();
        this.content = feed.getContent();
        this.hasImage = !feed.getImageList().isEmpty();
        this.hasVideo = !feed.getVideoList().isEmpty();
        this.numberOfContents = feed.getImageList().size() + feed.getVideoList().size();
        this.hearts = hearts;
        this.comments = feed.getCommentList().size();
        this.imageList = makeImageList(feed.getImageList());
        this.videoList = makeVideoList(feed.getVideoList());
    }

    private List<String> makeImageList(List<Image> imageList) {
        return imageList.stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }

    private List<String> makeVideoList(List<Video> videoList) {
        return videoList.stream()
                .map(Video::getUrl)
                .collect(Collectors.toList());
    }
}
