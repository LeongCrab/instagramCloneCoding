package com.example.demo.src.post;


import com.example.demo.common.exceptions.BaseException;

import com.example.demo.src.post.model.*;
import com.example.demo.src.post.entity.*;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;
    private final HeartRepository heartRepository;


    public PostPostRes createPost(Long jwtId, PostPostReq postPostReq){
        User user = userRepository.findByIdAndState(jwtId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        Post savePost = postRepository.save(postPostReq.toEntity(user));

        createImage(savePost, postPostReq);
        createVideo(savePost, postPostReq);

        return new PostPostRes(savePost.getId());
    }

    private void createImage(Post post, PostPostReq postPostReq) {
        List<String> imageList = postPostReq.getImageList();
        for(String url : imageList){
            Image image = Image.builder()
                    .url(url)
                    .post(post)
                    .build();
            imageRepository.save(image);
            log.info("추가된 사진 id :" + image.getId());
        }
    }

    private void createVideo(Post post, PostPostReq postPostReq) {
        List<String> videoList = postPostReq.getVideoList();
        for(String url : videoList){
            Video video = Video.builder()
                    .url(url)
                    .post(post)
                    .build();
            videoRepository.save(video);
            log.info("추가된 영상 id :" + video.getId());
        }
    }
    @Transactional(readOnly = true)
    public List<GetPostRes> getPosts(int size, int pageIndex) throws BaseException{
        try{
            PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
            Page<Post> postPage = postRepository.findAllByState(ACTIVE, pageRequest);
            Page<GetPostRes> dtoPage = postPage.map(this::makeGetPostRes);

            return dtoPage.getContent();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    @Transactional(readOnly = true)
    public List<GetPostRes> getPostsByLoginId(int size, int pageIndex, String loginId) {
        User user = userRepository.findByLoginIdAndState(loginId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        try{
            PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
            Page<Post> postPage = postRepository.findByUserIdAndState(user.getId(), ACTIVE, pageRequest);
            Page<GetPostRes> dtoPage = postPage.map(this::makeGetPostRes);

            return dtoPage.getContent();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private int getHearts(long postId){
        return heartRepository.countByPostIdAndState(postId, ACTIVE);
    }

    private List<String> getImageList(long postId) {
        List<Image> imageList = imageRepository.findAllByPostIdAndState(postId, ACTIVE);
        return imageList.stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
    }

    private List<String> getVideoList(long postId) {
        List<Video> videoList = videoRepository.findAllByPostIdAndState(postId, ACTIVE);
        return videoList.stream()
                .map(Video::getUrl)
                .collect(Collectors.toList());
    }
    public boolean existHeart(long jwtId, long postId) {
        Optional<Heart> existHeart = heartRepository.findByUserIdAndPostId(jwtId, postId);

        return existHeart.isPresent();
    }

    public void createHeart(long jwtId, long postId) {
        User user = userRepository.findByIdAndState(jwtId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        Post post = postRepository.findByIdAndState(postId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_POST));

        Heart heart = Heart.builder()
                .user(user)
                .post(post)
                .build();
        heartRepository.save(heart);
    }

    public void patchHeart(long jwtId, long postId) {
        Heart heart = heartRepository.findByUserIdAndPostId(jwtId, postId)
                .orElseThrow(() -> new BaseException(NOT_FIND_HEART));
        heart.patchHeart();
    }

    private GetPostRes makeGetPostRes(Post post) {
        Long postId = post.getId();
        int hearts = getHearts(postId);
        List<String> imageList = getImageList(postId);
        List<String> videoList = getVideoList(postId);

        return new GetPostRes(post, hearts, imageList, videoList);
    }
}
