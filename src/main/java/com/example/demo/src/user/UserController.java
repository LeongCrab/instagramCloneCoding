package com.example.demo.src.user;


import com.example.demo.common.Constant.LoginType;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


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
    @ResponseBody
    @PostMapping("/user")
    public BaseResponse<PostUserRes> createUser(@Valid @RequestBody PostUserReq postUserReq) {
        PostUserRes postUserRes = userService.createUser(postUserReq);
        return new BaseResponse<>(postUserRes);
    }


    /**
     * 회원 조회 API
     * [GET] /app/users
     * 회원 번호 및 아이디 검색 조회 API
     * [GET] /app/users? UserId=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/users") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String userId) {
        if(userId == null){
            List<GetUserRes> getUsersRes = userService.getUsers();
            return new BaseResponse<>(getUsersRes);
        }
        // Get Users
        List<GetUserRes> getUsersRes = userService.getUsersByUserId(userId);
        return new BaseResponse<>(getUsersRes);
    }


    /**
     * 회원 1명 조회 API
     * [GET] /app/users/:userId
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/users/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") Long userId) {
        GetUserRes getUserRes = userService.getUser(userId);
        return new BaseResponse<>(getUserRes);
    }


    /**
     * 비밀번호 변경 API
     * [PATCH] /app/users/change-password
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/users/change-password")
    public BaseResponse<String> updatePassword(@Valid @RequestBody PatchUserReq patchUserReq){
        Long jwtId = jwtService.getId();

        userService.updatePassword(jwtId, patchUserReq);

        String result = "비밀번호 변경 완료!!";
        return new BaseResponse<>(result);
    }


    /**
     * 유저 프로필 변경 API
     * [PATCH] /app/users/profile
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/users/profile")
    public BaseResponse<String> updateProfile(@Valid @RequestBody PatchProfileReq patchProfileReq){
        Long jwtId = jwtService.getId();
        userService.updateProfile(jwtId, patchProfileReq);

        String result = "프로필 변경 완료!!";
        return new BaseResponse<>(result);
    }


    /**
     * 유저 정보 삭제 API
     * [DELETE] /app/users
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/users")
    public BaseResponse<String> deleteUser(){
        Long jwtId = jwtService.getId();
        userService.deleteUser(jwtId);

        String result = "아이디 삭제 완료!!";
        return new BaseResponse<>(result);
    }


    /**
     * 로그인 API
     * [POST] /app/users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/users/login")
    public BaseResponse<PostLoginRes> logIn(@Valid @RequestBody PostLoginReq postLoginReq){
        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
        return new BaseResponse<>(postLoginRes);
    }


    /**
     * 유저 소셜 가입, 로그인 인증으로 리다이렉트 해주는 url
     * [GET] /app/users/:socialLoginType/login
     * @return void
     */
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
    @ResponseBody
    @PostMapping("/chat/{receiverId}")
    public BaseResponse<String> createUser(@PathVariable("receiverId") Long receiverId,@Valid @RequestBody PostChatReq postChatReq) {
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
