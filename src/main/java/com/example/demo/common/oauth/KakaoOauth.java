package com.example.demo.common.oauth;


import com.example.demo.src.user.model.KakaoOAuthToken;
import com.example.demo.src.user.model.KakaoUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

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
        log.info("redirectURL = " + redirectURL);

        return redirectURL;
        /*
         * https://kauth.kakao.com/oauth/authorize?client_id=44a90896f3ee78d0e03712d6cd025cfd&redirect_uri=http://localhost:9000/app/users/kakao/login/callback&response_type=code
         * &client_id="할당받은 id"&redirect_uri="access token 처리")
         * 로 Redirect URL을 생성하는 로직을 구성
         * */
    }
    public ResponseEntity<String> requestAccessToken(String code) {
        String KAKAO_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", KAKAO_REST_API_KEY);
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("grant_type", "authorization_code");

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                KAKAO_TOKEN_REQUEST_URL,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return responseEntity;
        }

        return null;
    }

    public KakaoOAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        log.info("response.getBody() = {}", response.getBody());

        KakaoOAuthToken kakaoOAuthToken= objectMapper.readValue(response.getBody(),KakaoOAuthToken.class);
        return kakaoOAuthToken;
    }

    public ResponseEntity<String> requestUserInfo(KakaoOAuthToken oAuthToken) {
        String KAKAO_USERINFO_REQUEST_URL="https://kapi.kakao.com/v2/user/me";

        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 카카오와 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                request,
                String.class
        );

        log.info("response.getBody() = {}", response.getBody());

        return response;
    }

    public KakaoUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException{
        KakaoUser kakaoUser = objectMapper.readValue(userInfoRes.getBody(), KakaoUser.class);
        return kakaoUser;
    }
}
