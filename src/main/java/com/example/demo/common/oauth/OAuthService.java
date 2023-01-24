package com.example.demo.common.oauth;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.LogRepository;
import com.example.demo.src.admin.entity.Log;
import com.example.demo.src.admin.model.GetUserRes;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.common.response.BaseResponseStatus.INVALID_OAUTH_TYPE;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOauth googleOauth;
    private final KakaoOauth kakaoOauth;
    private final HttpServletResponse response;
    private final UserService userService;
    private final JwtService jwtService;
    private final LogRepository logRepository;

    public void accessRequest(Constant.LoginType loginType) throws IOException {
        String redirectURL;
        switch (loginType){
            case GOOGLE:
                redirectURL= googleOauth.getOauthRedirectURL();
                break;
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
            case GOOGLE: {
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);

                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);

                if(userService.checkUserByLoginId(googleUser.getEmail())) {
                    GetUserRes getUserRes = userService.getUserByLoginId(googleUser.getEmail());
                    String jwtToken = jwtService.createJwt(getUserRes.getId());

                    return new GetSocialOAuthRes(jwtToken, getUserRes.getId(), oAuthToken.getAccess_token(), oAuthToken.getToken_type());
                } else {
                    PostUserRes postUserRes = userService.createOAuthUser(googleUser.toEntity());
                    return new GetSocialOAuthRes(postUserRes.getJwt(), postUserRes.getId(), oAuthToken.getAccess_token(), oAuthToken.getToken_type());
                }
            }
            case KAKAO:{
                ResponseEntity<String> accessTokenResponse = kakaoOauth.requestAccessToken(code);
                KakaoOAuthToken oAuthToken = kakaoOauth.getAccessToken(accessTokenResponse);

                ResponseEntity<String> userInfoResponse = kakaoOauth.requestUserInfo(oAuthToken);
                KakaoUser kakaoUser = kakaoOauth.getUserInfo(userInfoResponse);

                if(userService.checkUserByLoginId(kakaoUser.getKakao_account().getEmail())) {
                    GetUserRes getUserRes = userService.getUserByLoginId(kakaoUser.getKakao_account().getEmail());

                    String jwtToken = jwtService.createJwt(getUserRes.getId());
                    Log log = new Log(Constant.DataType.LOGIN, Constant.MethodType.CREATE, getUserRes.getId());
                    logRepository.save(log);
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
