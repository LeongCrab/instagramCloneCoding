package com.example.demo.src.post.model;

import com.example.demo.src.post.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GetPostRes {
    private Long id;
    private Long userId;
    private String content;
    private Boolean hasImage;
    private Boolean hasVideo;
    private Integer numberOfFiles;
    private Integer hearts;
    private List<String> imageList;
    private List<String> videoList;

    public GetPostRes(Post post, int hearts, List<String> imageList, List<String> videoList) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.content = post.getContent();
        this.hasImage = post.getHasImage();
        this.hasVideo = post.getHasVideo();
        this.numberOfFiles = post.getNumberOfFiles();
        this.hearts = hearts;
        this.imageList = imageList;
        this.videoList = videoList;
    }
}
