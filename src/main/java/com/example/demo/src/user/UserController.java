package com.example.demo.src.user;


import com.example.demo.common.Constant.LoginType;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Tag(name= "user 도메인", description = "회원, 마이페이지, 팔로우, 채팅 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app")
public class UserController {

    private final UserService userService;
    private final OAuthService oAuthService;
    private final JwtService jwtService;


    /**
     * 회원가입 API
     * [POST] /app/users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @Operation(summary = "회원가입", description = "회원 정보를 받아 회원가입합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "정보 입력 오류"),
            @ApiResponse(responseCode = "500", description = "서버와 통신 에러")
    })
    @ResponseBody
    @PostMapping("/user")
    public BaseResponse<PostUserRes> createUser(@Valid @RequestBody PostUserReq postUserReq) {
        PostUserRes postUserRes = userService.createUser(postUserReq);
        return new BaseResponse<>(postUserRes);
    }


    /**
     * 비밀번호 변경 API
     * [PATCH] /app/users/change-password
     * @return BaseResponse<String>
     */
    @Operation(summary = "비밀번호 변경", description = "JWT 토큰을 받고 해당 계정의 비밀번호를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "변경할 비밀번호를 입력하세요"),
            @ApiResponse(responseCode = "400", description = "비밀번호는 6~20자로 입력해주세요"),
            @ApiResponse(responseCode = "401", description = "JWT를 입력해주세요")

    })
    @ResponseBody
    @PatchMapping("/users/change-password")
    public BaseResponse<String> updatePassword(@Valid @RequestBody PatchUserReq patchUserReq){

        Long jwtId = jwtService.getId();

        userService.updatePassword(jwtId, patchUserReq);

        String result = "비밀번호 변경 완료";
        return new BaseResponse<>(result);
    }


    /**
     * 유저 프로필 변경 API
     * [PATCH] /app/users/profile
     * @return BaseResponse<String>
     */
    @Operation(summary = "프로필 변경", description = "JWT 토큰을 받고 해당 계정의 프로필을 변경합니다.")
    @ResponseBody
    @PatchMapping("/users/profile")
    public BaseResponse<String> updateProfile(@Valid @RequestBody PatchProfileReq patchProfileReq){
        Long jwtId = jwtService.getId();
        userService.updateProfile(jwtId, patchProfileReq);

        String result = "프로필 변경 완료";
        return new BaseResponse<>(result);
    }


    /**
     * 유저 탈퇴 API
     * [PATCH] /app/users
     * @return BaseResponse<String>
     */
    @Operation(summary = "회원탈퇴", description = "JWT 토큰을 받고 해당 계정을 삭제합니다.")
    @ResponseBody
    @PatchMapping("/users/delete")
    public BaseResponse<String> deleteUser(){
        Long jwtId = jwtService.getId();
        userService.deleteUser(jwtId);

        String result = "유저 탈퇴 완료";
        return new BaseResponse<>(result);
    }


    /**
     * 로그인 API
     * [POST] /app/users/login
     * @return BaseResponse<PostLoginRes>
     */
    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인합니다.")
    @ResponseBody
    @PostMapping("/users/login")
    public BaseResponse<PostLoginRes> logIn(@Valid @RequestBody PostLoginReq postLoginReq){
        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
        return new BaseResponse<>(postLoginRes);
    }


    /**
     * 유저 소셜 가입, 로그인 인증으로 리다이렉트 해주는 url
     * [GET] /app/users/:loginType/login
     *
     */
    @Operation(summary = "소셜 회원가입/로그인", description = "소셜 서비스로부터 회원 정보를 받아 회원가입합니다.")
    @GetMapping("/users/{loginType}/login")
    public void loginRedirect(@PathVariable(name="loginType") String loginPath) throws IOException {
        LoginType loginType= LoginType.valueOf(loginPath.toUpperCase());
        oAuthService.accessRequest(loginType);
    }


    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param loginPath (GOOGLE, APPLE, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
     */
    @Operation(hidden = true)
    @ResponseBody
    @GetMapping("/users/{loginType}/login/callback")
    public BaseResponse<GetSocialOAuthRes> loginCallback(
            @PathVariable(name = "loginType") String loginPath,
            @RequestParam(name = "code") String code
    ) throws IOException, BaseException{
        log.info(">> 소셜 로그인 API 서버로부터 받은 code : {}", code);
        LoginType loginType = LoginType.valueOf(loginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLoginOrJoin(loginType,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }


    /**
     * 팔로우 API
     * [POST] /app/follow/:userId
     * @return BaseResponse<String>
     */
    @Operation(summary = "팔로우/언팔로우", description = "대상 유저를 팔로우/언팔로우한다.")
    @ResponseBody
    @PostMapping("/follow/{userId}")
    public BaseResponse<String> follow(@PathVariable("userId") long userId) {
        Long jwtId = jwtService.getId();

        boolean existFollow = userService.existFollow(jwtId, userId);

        String result;
        if(existFollow){
            userService.toggleFollow(jwtId, userId);
            result = "팔로우 변경 완료";
        } else{
            userService.createFollow(jwtId, userId);
            result = "팔로우 생성 완료";
        }

        return new BaseResponse<>(result);
    }


    /**
     * 마이페이지 조회 API
     * [GET] /app/myPage/:loginId
     * @return BaseResponse<GetUserRes>
     */
    @Operation(summary = "마이페이지 조회", description = "유저 아이디로 마이페이지 정보를 조회한다.")
    @ResponseBody
    @GetMapping("/myPage/{userId}")
    public BaseResponse<GetMyPageRes> getMyPage(@PathVariable("userId") Long userId) {
        GetMyPageRes getMyPageRes = userService.getMyPage(userId);
        return new BaseResponse<>(getMyPageRes);
    }


    /**
     * 마이페이지 게시글 조회 API
     * [GET] /app/myPage/:loginId/feeds?pageIndex=
     * @return BaseResponse<GetUserRes>
     */
    @Operation(summary = "마이페이지 게시글 사진 조회", description = "유저 아이디로 마이페이지에 표시할 게시글 첫 사진을 9장씩 페이징으로 조회한다.")
    @ResponseBody
    @GetMapping("/myPage/{userId}/feeds")
    public BaseResponse<GetMyPageFeedsRes> getMyPageFeeds(@PathVariable("userId") Long userId, @RequestParam int pageIndex) throws BaseException{
        GetMyPageFeedsRes getMyPageFeedsRes = userService.getMyPageFeeds(userId, pageIndex);

        return new BaseResponse<>(getMyPageFeedsRes);
    }


    /**
     * 채팅 API
     * [POST] /app/chat/:receiverId
     * @return BaseResponse<String>
     */
    // Body
    @Operation(summary = "채팅 보내기", description = "유저 아이디로 받는 사람을 정하고 채팅을 보낸다.")
    @ResponseBody
    @PostMapping("/chat/{receiverId}")
    public BaseResponse<String> createUser(@PathVariable("receiverId") Long receiverId, @Valid @RequestBody PostChatReq postChatReq) {
        Long jwtId = jwtService.getId();
        userService.createChat(jwtId, receiverId, postChatReq);

        String result = "채팅 보내기 성공";
        return new BaseResponse<>(result);
    }


    /**
     * 채팅 목록 조회 API
     * [GET] /app/chats?size=&pageIndex=&userId=
     * @return BaseResponse<GetChatRes>
     */
    @Operation(summary = "채팅 목록 조회", description = "나와 채팅한 기록이 있는 유저 목록을 불러온다.")
    @ResponseBody
    @GetMapping("/chats")
    public BaseResponse<List<GetChatRes>> getChatList(
            @RequestParam int size,
            @RequestParam int pageIndex,
            @RequestParam Long receiverId
    ) {
        Long jwtId = jwtService.getId();
        List<GetChatRes> getChatResList = userService.getChatList(size, pageIndex, jwtId, receiverId);

        return new BaseResponse<>(getChatResList);

    }
}
