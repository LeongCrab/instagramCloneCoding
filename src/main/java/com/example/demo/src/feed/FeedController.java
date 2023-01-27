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
    @GetMapping("")
    public BaseResponse<List<GetFeedRes>> getFeeds(
            @RequestParam int size,
            @RequestParam int pageIndex,
            @RequestParam(required = false) Long userId
    ) {
        if(userId == null){
            List<GetFeedRes> getFeedResList = feedService.getFeeds(size, pageIndex);
            return new BaseResponse<>(getFeedResList);
        }
        List<GetFeedRes> getFeedResList = feedService.getFeedsByLoginId(size, pageIndex, userId);
        return new BaseResponse<>(getFeedResList);
    }

    /**
     * 게시글 수정 API
     * [PATCH] /app/feeds/:feedId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{feedId}")
    public BaseResponse<String> modifyFeed(@PathVariable("feedId") Long feedId, @Valid @RequestBody PatchFeedReq patchFeedReq){
        Long jwtId = jwtService.getId();

        feedService.modifyFeed(jwtId, feedId, patchFeedReq);

        String result = "게시글 수정 완료";
        return new BaseResponse<>(result);
    }

    /**
     * 게시글 삭제 API
     * [PATCH] /app/feeds/:feedId/delete
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{feedId}/delete")
    public BaseResponse<String> deleteFeed(@PathVariable("feedId") long feedId){
        Long jwtId = jwtService.getId();

        feedService.deleteFeed(jwtId, feedId);

        String result = "게시글 삭제 완료";
        return new BaseResponse<>(result);
    }

    /**
     * 좋아요 API
     * [POST] /app/feeds/:feedId/heart
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{feedId}/heart")
    public BaseResponse<String> heart(@PathVariable("feedId") long feedId) {
        Long jwtId = jwtService.getId();
        boolean existHeart = feedService.existHeart(jwtId, feedId);

        String result;
        if(existHeart){
            feedService.toggleHeart(jwtId, feedId);
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
     * [PATCH] /app/feeds/:feedId/comment/:commentId/delete
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{feedId}/comment/{commentId}/delete")
    public BaseResponse<String> deleteComment(@PathVariable("commentId") long commentId) {
        Long jwtId = jwtService.getId();
        feedService.deleteComment(jwtId, commentId);
        String result = "댓글 삭제 성공";
        return new BaseResponse<>(result);
    }

    /**
     * 게시글 신고 API
     * [POST] /app/feeds/:feedId/report
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{feedId}/report")
    public BaseResponse<String> createFeedReport(@PathVariable("feedId") long feedId, @RequestBody PostReportReq postReportReq) {
        feedService.createFeedReport(feedId, postReportReq);
        String result = "게시물 신고 성공";
        return new BaseResponse<>(result);
    }

    /**
     * 댓글 신고 API
     * [POST] /app/feeds/comment/:commentId/report
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/comment/{commentId}/report")
    public BaseResponse<String> createCommentReport(@PathVariable("commentId") long commentId, @RequestBody PostReportReq postReportReq) {
        feedService.createCommentReport(commentId, postReportReq);
        String result = "댓글 신고 성공";
        return new BaseResponse<>(result);
    }
}
