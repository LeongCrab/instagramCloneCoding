package com.example.demo.common.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth{
    @Value("${spring.OAuth2.kakao.url}")
    private String KAKAO_URL;
    @Value("${spring.OAuth2.kakao.rest-api-key}")
    private String KAKAO_REST_API_KEY;
    @Value("${spring.OAuth2.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;
    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", KAKAO_REST_API_KEY);
        params.put("redirect_uri", KAKAO_REDIRECT_URI);
        //parameter를 형식에 맞춰 구성해주는 함수
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = KAKAO_URL + "?" + parameterString;
        log.info("redirectURL = ", redirectURL);

        return redirectURL;
        /*
         * https://kauth.kakao.com/oauth/authorize?client_id=44a90896f3ee78d0e03712d6cd025cfd&redirect_uri=http://localhost:9000/app/users/kakao/login/callback&response_type=code
         * &client_id="할당받은 id"&redirect_uri="access token 처리")
         * 로 Redirect URL을 생성하는 로직을 구성
         * */
    }
}
