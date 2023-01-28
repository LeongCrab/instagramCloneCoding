package com.example.demo.src.admin;


import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.*;
import com.example.demo.src.history.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name= "admin 도메인", description = "admin 페이지 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;


    /**
     * 회원 조회 API
     * [GET] /admin/users?pageIndex=
     *
     * @return BaseResponse<List<GetUserRes>>
     */
    @Operation(summary = "회원 목록 조회", description = "회원 목록을 조회한다.")
    @ResponseBody
    @GetMapping("/users")
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam int pageIndex, @Valid @RequestBody(required = false) GetUserReq getUserReq) {
        List<GetUserRes> getUserResList = adminService.getUsers(pageIndex, getUserReq);

        return new BaseResponse<>(getUserResList);
    }


    /**
     * 회원 상세 조회 API
     * [GET] /admin/users/:userId
     *
     * @return BaseResponse<GetUserInfoRes>
     */
    @Operation(summary = "회원 상세 조회", description = "회원 상세 정보를 조회한다.")
    @ResponseBody
    @GetMapping("/users/{userId}")
    public BaseResponse<GetUserInfoRes> getUser(@PathVariable("userId") Long userId) {
        GetUserInfoRes getUserInfoRes = adminService.getUser(userId);

        return new BaseResponse<>(getUserInfoRes);
    }


    /**
     * 회원 정지 API
     * [PATCH] /admin/users/:userId/ban
     *
     * @return BaseResponse<String>
     */
    @Operation(summary = "회원 정지", description = "회원 상태를 정지 상태로 바꾼다.")
    @ResponseBody
    @PatchMapping("/users/{userId}/ban")
    public BaseResponse<String> banUser(@PathVariable("userId") Long userId) {
        String result = adminService.banUser(userId);

        return new BaseResponse<>(result);
    }

    /**
     * 피드 조회 API
     * [GET] /admin/feeds?pageIndex=
     *
     * @return BaseResponse<List<GetFeedRes>>
     */
    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회한다.")
    @ResponseBody
    @GetMapping("/feeds")
    public BaseResponse<List<GetFeedRes>> getFeeds(@RequestParam int pageIndex, @Valid @RequestBody(required = false) GetFeedReq getFeedReq) {
        List<GetFeedRes> getFeedResList = adminService.getFeeds(pageIndex, getFeedReq);

        return new BaseResponse<>(getFeedResList);
    }

    /**
     * 피드 상세 조회 API
     * [GET] /admin/feeds/:feedId
     *
     * @return BaseResponse<GetFeedInfoRes>
     */
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보를 조회한다.")
    @ResponseBody
    @GetMapping("/feeds/{feedId}")
    public BaseResponse<GetFeedInfoRes> getFeed(@PathVariable("feedId") Long feedId) {
        GetFeedInfoRes getFeedInfoRes = adminService.getFeed(feedId);

        return new BaseResponse<>(getFeedInfoRes);
    }

    /**
     * 피드 강제 삭제 API
     * [PATCH] /admin/feeds/:feedId/ban
     *
     * @return BaseResponse<String>
     */
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제하고 달린 댓글도 전부 비활성화한다.")
    @ResponseBody
    @PatchMapping("/feeds/{feedId}/ban")
    public BaseResponse<String> banFeed(@PathVariable("feedId") Long feedId) {
        String result = adminService.banFeed(feedId);

        return new BaseResponse<>(result);
    }

    /**
     * 신고 조회 API
     * [GET] /admin/reports?pageIndex=
     *
     * @return BaseResponse<List<GetReportRes>>
     */
    @Operation(summary = "신고 목록 조회", description = "신고 목록을 조회한다.")
    @ResponseBody
    @GetMapping("/reports")
    public BaseResponse<List<GetReportRes>> getReports(@RequestParam int pageIndex) {
        List<GetReportRes> getReportResList = adminService.getReports(pageIndex);

        return new BaseResponse<>(getReportResList);
    }

    /**
     * 신고 삭제 API
     * [PATCH] /admin/reports/:reportId/delete
     *
     * @return BaseResponse<String>
     */
    @Operation(summary = "신고 삭제", description = "신고를 삭제한다.")
    @ResponseBody
    @PatchMapping("/reports/{reportId}/delete")
    public BaseResponse<String> deleteReports(@PathVariable("reportId") Long reportId) {
        String result = adminService.deleteReport(reportId);

        return new BaseResponse<>(result);
    }

    /**
     * 회원 히스토리 조회 API
     * [GET] /admin/histories/users?pageIndex=
     *
     * @return BaseResponse<List<GetUserHistoryRes>>
     */
    @Operation(summary = "회원 히스토리 조회", description = "회원 정보 변경 내역을 조회한다.")
    @ResponseBody
    @GetMapping("/history/users")
    public BaseResponse<List<GetUserHistoryRes>> getUserHistory(@RequestParam int pageIndex, @Valid @RequestBody(required = false) GetHistoryReq getHistoryReq) {
        List<GetUserHistoryRes> GetUserHistoryResList = adminService.getUserHistory(pageIndex, getHistoryReq);

        return new BaseResponse<>(GetUserHistoryResList);
    }

    /**
     * 게시글 히스토리 조회 API
     * [GET] /admin/history/feeds?pageIndex=
     *
     * @return BaseResponse<List<GetFeedHistoryRes>>
     */
    @Operation(summary = "게시글 히스토리 조회", description = "게시글 정보 변경 내역을 조회한다.")
    @ResponseBody
    @GetMapping("/history/feeds")
    public BaseResponse<List<GetFeedHistoryRes>> getFeedHistory(@RequestParam int pageIndex, @Valid @RequestBody(required = false) GetHistoryReq getHistoryReq) {
        List<GetFeedHistoryRes> GetFeedHistoryResList = adminService.getFeedHistory(pageIndex, getHistoryReq);

        return new BaseResponse<>(GetFeedHistoryResList);
    }

    /**
     * 댓글 히스토리 조회 API
     * [GET] /admin/history/comments?pageIndex=
     *
     * @return BaseResponse<List<GetCommentHistoryRes>>
     */
    @Operation(summary = "댓글 히스토리 조회", description = "댓글 정보 변경 내역을 조회한다.")
    @ResponseBody
    @GetMapping("/history/comments")
    public BaseResponse<List<GetCommentHistoryRes>> getCommentHistory(@RequestParam int pageIndex, @Valid @RequestBody(required = false) GetHistoryReq getHistoryReq) {
        List<GetCommentHistoryRes> GetCommentHistoryResList = adminService.getCommentHistory(pageIndex, getHistoryReq);

        return new BaseResponse<>(GetCommentHistoryResList);
    }

    /**
     * 신고 조회 API
     * [GET] /admin/history/reports?pageIndex=
     *
     * @return BaseResponse<List<GetReportRes>>
     */
    @Operation(summary = "신고 히스토리 조회", description = "신고 내역을 조회한다.")
    @ResponseBody
    @GetMapping("/history/reports")
    public BaseResponse<List<GetReportHistoryRes>> getReports(@RequestParam int pageIndex, @Valid @RequestBody(required = false) GetHistoryReq getHistoryReq) {
        List<GetReportHistoryRes> getReportHistoryResResList = adminService.getReportHistory(pageIndex, getHistoryReq);

        return new BaseResponse<>(getReportHistoryResResList);
    }
}

