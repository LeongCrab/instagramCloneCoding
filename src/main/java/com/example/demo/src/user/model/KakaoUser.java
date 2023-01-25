package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.src.user.entity.User;
import lombok.*;

import java.time.LocalDate;

//카카오(서드파티)로 액세스 토큰을 보내 받아올 구글에 등록된 사용자 정보
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUser {
    public Long id;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    @ToString
    public static class KakaoAccount {
        private String email;
        private String birthday;
    }

    @Getter
    @ToString
    public static class Properties {
        private String nickname;
    }

    public User toEntity() {
        return User.builder()
                .loginId(this.getKakao_account().getEmail())
                .password("NONE")
                .phone("NONE")
                .name(this.getProperties().getNickname())
                .loginType(Constant.LoginType.KAKAO)
                .birthday(birthdayFormat(this.getKakao_account().getBirthday()))
                .birthYear("NONE")
                .privacyExpiredAt(LocalDate.now().plusYears(1))
                .build();
    }

    private String birthdayFormat(String str) {
        return new StringBuffer(str)
                .insert(2,'-')
                .toString();
    }
}
