package com.example.demo.src.post;


import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.post.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final JwtService jwtService;


    /**
     * 게시글 작성 API
     * [POST] /post
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
}
