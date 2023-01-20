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
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.DATABASE_ERROR;
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
            imageRepository.save(image);
            log.info("추가된 사진 id :" + image.getId());
        }
    }

    public void createVideo(Post post, PostPostReq postPostReq) {
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
    public List<GetPostRes> getPosts(int pageSize, int pageIdx) throws BaseException{
        try{
            PageRequest pageRequest = PageRequest.of(pageIdx, pageSize, Sort.by(Sort.Direction.DESC, "updatedAt"));
            Page<Post> postPage = postRepository.findAllByState(ACTIVE, pageRequest);
            Page<GetPostRes> dtoPage = postPage.map((post)-> {
                Long postId = post.getId();
                List<String> imageList = getImageList(postId);
                List<String> videoList = getVideoList(postId);

                return new GetPostRes(post, imageList, videoList);
            });
            List<GetPostRes> GetPostResList = dtoPage.getContent();

            return GetPostResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    @Transactional(readOnly = true)
    public List<GetPostRes> getPostsByUserId(int pageSize, int pageIdx, String userId) {
        User user = userRepository.findByUserIdAndState(userId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        try{
            PageRequest pageRequest = PageRequest.of(pageIdx, pageSize, Sort.by(Sort.Direction.DESC, "updatedAt"));
            Page<Post> postPage = postRepository.findByUserIdAndState(user.getId(), ACTIVE, pageRequest);
            Page<GetPostRes> dtoPage = postPage.map((post)-> {
                Long postId = post.getId();
                List<String> imageList = getImageList(postId);
                List<String> videoList = getVideoList(postId);

                return new GetPostRes(post, imageList, videoList);
            });
            List<GetPostRes> GetPostResList = dtoPage.getContent();

            return GetPostResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private List<String> getImageList(Long postId) {
        List<Image> imageList = imageRepository.findAllByPostIdAndState(postId, ACTIVE);
        return imageList.stream()
                .map((image) -> image.getUrl())
                .collect(Collectors.toList());
    }

    private List<String> getVideoList(Long postId) {
        List<Video> videoList = videoRepository.findAllByPostIdAndState(postId, ACTIVE);
        return videoList.stream()
                .map((video) -> video.getUrl())
                .collect(Collectors.toList());
    }
}
