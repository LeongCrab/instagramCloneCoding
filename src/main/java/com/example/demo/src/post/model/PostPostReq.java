package com.example.demo.src.post.model;

import com.example.demo.src.post.entity.Post;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPostReq {
    @Size(max= 2200, message = "[Post] 최대 2,200자까지 입력 가능합니다.")
    private String content;
    @Size(max= 10, message = "사진은 최대 10개까지 등록 가능합니다.")
    private List<String> imageList = new ArrayList<>();
    @Size(max= 10, message = "동영상은 최대 10개까지 등록 가능합니다.")
    private List<String> videoList = new ArrayList<>();

    public Post toEntity(User user) {
        return Post.builder()
                .content(this.content)
                .hasImage(!this.imageList.isEmpty())
                .hasVideo(!this.videoList.isEmpty())
                .numberOfFiles(this.imageList.size() + this.videoList.size())
                .user(user)
                .build();
    }
}
