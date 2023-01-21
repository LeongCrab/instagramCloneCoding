package com.example.demo.src.feed;


import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.feed.model.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@RequiredArgsConstructor
@RestController
@RequestMapping("/app/feeds")
public class FeedController {
    private final FeedService feedService;
    private final JwtService jwtService;


    /**
     * 게시글 작성 API
     * [POST] /app/feeds
     * @return BaseResponse<PostFeedRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostFeedRes> createFeed(@Valid @RequestBody PostFeedReq postFeedReq) {
        Long jwtId = jwtService.getId();
        PostFeedRes postFeedRes = feedService.createFeed(jwtId, postFeedReq);
        return new BaseResponse<>(postFeedRes);
    }

    /**
     * 게시글 조회 API
     * [GET] /app/feeds?size=&pageIndex=
     * 작성자 아이디로 검색 조회 API
     * [GET] /app/feeds?size=&pageIndex=&userId=
     * @return BaseResponse<List<GetFeedRes>>
     */
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/feeds/pageSize/pageIdx?userId=userId
    public BaseResponse<List<GetFeedRes>> getFeeds(
            @RequestParam(required = false) int size,
            @RequestParam(required = false) int pageIndex,
            @RequestParam(required = false) String loginId) {
        if(loginId == null){
            List<GetFeedRes> getFeedResList = feedService.getFeeds(size, pageIndex);
            return new BaseResponse<>(getFeedResList);
        }
        List<GetFeedRes> getFeedResList = feedService.getFeedsByLoginId(size, pageIndex, loginId);
        return new BaseResponse<>(getFeedResList);
    }

    /**
     * 좋아요 API
     * [POST] /app/feeds/:feedId/heart
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{feedId}/heart")
    public BaseResponse<String> createHeart(@PathVariable("feedId") long feedId) {
        Long jwtId = jwtService.getId();
        boolean existHeart = feedService.existHeart(jwtId, feedId);

        String result;
        if(existHeart){
            feedService.patchHeart(jwtId, feedId);
            result = "하트 변경 완료";
        } else{
            feedService.createHeart(jwtId, feedId);
            result = "하트 생성 완료";
        }

        return new BaseResponse<>(result);
    }

    /**
     * 댓글 작성 API
     * [POST] /app/feeds/:feedId/comment
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{feedId}/comment")
    public BaseResponse<String> createComment(@PathVariable("feedId") long feedId, @RequestBody PostCommentReq postCommentReq) {
        Long jwtId = jwtService.getId();
        feedService.createComment(jwtId, feedId, postCommentReq);
        String result = "댓글 작성 성공";
        return new BaseResponse<>(result);
    }

    /**
     * 댓글 조회 API
     * [GET] /app/feeds/:feedId/comment
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/{feedId}/comments")
    public BaseResponse<List<GetCommentRes>> getComment(@PathVariable("feedId") long feedId) {
        Long jwtId = jwtService.getId();
        List<GetCommentRes> getCommentResList = feedService.getCommentsByFeedId(feedId);

        return new BaseResponse<>(getCommentResList);
    }

    /**
     * 댓글 수정 API
     * [PATCH] /app/feeds/:feedId/comment/:commentId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{feedId}/comment/{commentId}")
    public BaseResponse<String> patchComment(@PathVariable("commentId") long commentId, @RequestBody PostCommentReq postCommentReq) {
        Long jwtId = jwtService.getId();
        feedService.patchComment(jwtId, commentId, postCommentReq);
        String result = "댓글 수정 성공";
        return new BaseResponse<>(result);
    }

    /**
     * 댓글 삭제 API
     * [DELETE] /app/feeds/:feedId/comment/:commentId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{feedId}/comment/{commentId}")
    public BaseResponse<String> deleteComment(@PathVariable("commentId") long commentId) {
        Long jwtId = jwtService.getId();
        feedService.deleteComment(jwtId, commentId);
        String result = "댓글 삭제 성공";
        return new BaseResponse<>(result);
    }
}
