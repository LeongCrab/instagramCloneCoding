package com.example.demo.src.feed.model;

import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostFeedReq {
    @Size(max= 2200, message = "게시글 내용은 최대 2,200자까지 입력 가능합니다.")
    private String content;
    @Size(max= 10, message = "게시글 사진은 최대 10개까지 등록 가능합니다.")
    private List<String> imageList = new ArrayList<>();
    @Size(max= 10, message = "게시글 영상은 최대 10개까지 등록 가능합니다.")
    private List<String> videoList = new ArrayList<>();

    public Feed toEntity(User user) {
        return Feed.builder()
                .content(this.content)
                .user(user)
                .build();
    }
}
