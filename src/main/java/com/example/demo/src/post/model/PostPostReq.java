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
    @Size(max= 2200)
    private String content;
    private List<String> imageList = new ArrayList<>();
    private List<String> videoList = new ArrayList<>();

    public Post toEntity(User user) {
        return Post.builder()
                .content(this.content)
                .user(user)
                .build();
    }
}
