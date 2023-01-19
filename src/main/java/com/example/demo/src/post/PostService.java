package com.example.demo.src.post;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.post.entity.Image;
import com.example.demo.src.post.entity.Video;
import com.example.demo.src.post.model.PostPostReq;
import com.example.demo.src.post.model.PostPostRes;
import com.example.demo.src.post.entity.Post;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_USER;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;


    public PostPostRes createPost(Long jwtId, PostPostReq postPostReq){
        User user = userRepository.findByIdAndState(jwtId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        Post savePost = postRepository.save(postPostReq.toEntity(user));

        createImage(savePost, postPostReq);
        createVideo(savePost, postPostReq);

        return new PostPostRes(savePost.getId());
    }

    public void createImage(Post post, PostPostReq postPostReq) {
        List<String> imageList = postPostReq.getImageList();
        for(String url : imageList){
            Image image = Image.builder()
                    .url(url)
                    .post(post)
                    .build();
            Image saveImage = imageRepository.save(image);
            log.info("추가된 사진 id :" + saveImage.getId());
        }
    }

    public void createVideo(Post post, PostPostReq postPostReq) {
        List<String> videoList = postPostReq.getVideoList();
        for(String url : videoList){
            Video video = Video.builder()
                    .url(url)
                    .post(post)
                    .build();
            Video saveVideo = videoRepository.save(video);
            log.info("추가된 영상 id :" + saveVideo.getId());
        }
    }
}
