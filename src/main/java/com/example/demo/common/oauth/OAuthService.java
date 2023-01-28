package com.example.demo.common.oauth;

import com.example.demo.common.Constant;
import com.example.demo.common.Constant.State;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.model.GetUserRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.common.response.BaseResponseStatus.INVALID_OAUTH_TYPE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_USER;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final KakaoOauth kakaoOauth;
    private final HttpServletResponse response;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public void accessRequest(Constant.LoginType loginType) throws IOException {
        String redirectURL;
        switch (loginType){
            case KAKAO:
                redirectURL= kakaoOauth.getOauthRedirectURL();
                break;
            default:
                throw new BaseException(INVALID_OAUTH_TYPE);
        }

        response.sendRedirect(redirectURL);
    }


    public GetSocialOAuthRes oAuthLoginOrJoin(Constant.LoginType loginType, String code) throws IOException {

        switch (loginType) {
            case KAKAO:{
                ResponseEntity<String> accessTokenResponse = kakaoOauth.requestAccessToken(code);
                KakaoOAuthToken oAuthToken = kakaoOauth.getAccessToken(accessTokenResponse);

                ResponseEntity<String> userInfoResponse = kakaoOauth.requestUserInfo(oAuthToken);
                KakaoUser kakaoUser = kakaoOauth.getUserInfo(userInfoResponse);
                String email = kakaoUser.getKakao_account().getEmail();

                if(userService.checkUserByLoginId(email)) {
                    GetUserRes getUserRes = userService.getUserByLoginId(email);
                    User user = userRepository.findByLoginIdAndState(email, State.ACTIVE)
                            .orElseThrow(()-> new BaseException(NOT_FIND_USER));
                    String jwtToken = jwtService.createJwt(getUserRes.getId());
                    user.updateLogin();

                    return new GetSocialOAuthRes(jwtToken, getUserRes.getId(), oAuthToken.getAccess_token(), oAuthToken.getToken_type());
                } else {
                    PostUserRes postUserRes = userService.createOAuthUser(kakaoUser.toEntity());
                    return new GetSocialOAuthRes(postUserRes.getJwt(), postUserRes.getId(), oAuthToken.getAccess_token(), oAuthToken.getToken_type());
                }
            }
            default: {
                throw new BaseException(INVALID_OAUTH_TYPE);
            }
        }
    }
}
