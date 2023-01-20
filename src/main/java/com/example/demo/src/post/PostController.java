package com.example.demo.src.post;


import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.post.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final JwtService jwtService;


    /**
     * 게시글 작성 API
     * [POST] /posts
     * @return BaseResponse<PostPostRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostRes> createPost(@Valid @RequestBody PostPostReq postPostReq) {
        Long jwtId = jwtService.getId();
        PostPostRes postPostRes = postService.createPost(jwtId, postPostReq);
        return new BaseResponse<>(postPostRes);
    }

    /**
     * 게시글 조회 API
     * [GET] /posts/pageSize/pageIdx
     * 작성자 아이디로 검색 조회 API
     * [GET] /posts?userId=
     * @return BaseResponse<List<GetPostRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{pageSize}/{pageIdx}") // (GET) 127.0.0.1:9000/posts/pageSize/pageIdx?userId=userId
    public BaseResponse<List<GetPostRes>> getPosts(@PathVariable("pageSize") int pageSize, @PathVariable("pageIdx") int pageIdx, @RequestParam(required = false) String userId) {
        if(userId == null){
            List<GetPostRes> getUsersRes = postService.getPosts(pageSize, pageIdx);
            return new BaseResponse<>(getUsersRes);
        }
        // Get Posts
        List<GetPostRes> getUsersRes = postService.getPostsByUserId(pageSize, pageIdx, userId);
        return new BaseResponse<>(getUsersRes);
    }

    /**
     * 좋아요 API
     * [POST] /posts/heart/:postId
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/heart/{postId}")
    public BaseResponse<String> createHeart(@PathVariable("postId") long postId) {
        Long jwtId = jwtService.getId();
        boolean existHeart = postService.existHeart(jwtId, postId);

        String result;
        if(existHeart){
            postService.patchHeart(jwtId, postId);
            result = "하트 변경 완료";
        } else{
            postService.createHeart(jwtId, postId);
            result = "하트 생성 완료";
        }

        return new BaseResponse<>(result);
    }
}
