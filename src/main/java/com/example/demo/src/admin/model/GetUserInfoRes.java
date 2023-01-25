package com.example.demo.src.admin.model;

import com.example.demo.src.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserInfoRes {
    private Long id;
    private String loginId;
    private String phone;
    private String name;
    private String birthday;
    private String birthYear;
    private String privacyExpiredAt;
    private String loginType;
    private String profileText;
    private String profileImage;
    private String createdAt;
    private String updatedAt;
    private String userState;
    private String lastLogin;

    public GetUserInfoRes(User user, String lastLogin){
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.phone = user.getPhone();
        this.name = user.getName();
        this.birthday = user.getBirthday();
        this.birthYear = user.getBirthYear();
        this.privacyExpiredAt = user.getPrivacyExpiredAt().toString();
        this.loginType = user.getLoginType().toString();
        this.profileImage = user.getProfileImage();
        this.profileText = user.getProfileText();
        this.createdAt = user.getCreatedAt().toString();
        this.updatedAt = user.getUpdatedAt().toString();
        this.userState = user.getUserState().toString();
        this.lastLogin = lastLogin;
    }
}

