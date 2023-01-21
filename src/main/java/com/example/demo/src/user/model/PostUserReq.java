package com.example.demo.src.user.model;

import com.example.demo.common.Constant;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    @NotBlank(message= "Insert phone number")
    @Pattern(regexp = "^[0-9]+$", message = "[phone number] Invalid format")
    @Size(max=20, message = "[phone number] maximum 20 characters")
    private String phone;
    @NotBlank(message= "Insert name")
    @Size(max=20, message = "[name] maximum 20 characters")
    private String name;
    @NotBlank(message= "Insert login id")
    @Pattern(regexp = "^[a-zA-Z0-9._]+$", message= "[login id] Invalid format")
    @Size(max=20, message = "[login id] maximum 20 characters")
    private String loginId;
    @NotBlank(message= "Insert password")
    @Size(min=6, max=20, message = "[password] 6 ~ 20 characters")
    private String password;
    @NotBlank(message = "Insert birthday")
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-([012][0-9]|3[01])", message= "[birthday] Invalid format")
    private String birthday; //'YYYY-MM-DD'

    //가입 시 자동으로 개인 정보 만료 날짜 1년 추가
    private LocalDate privacyExpiredAt = LocalDate.now().plusYears(1);

    protected Constant.LoginType loginType;

    public User toEntity() {
        return User.builder()
                .phone(this.phone)
                .name(this.name)
                .loginId(this.loginId)
                .password(this.password)
                .birthday(this.birthday)
                .loginType(this.loginType)
                .privacyExpiredAt(this.privacyExpiredAt)
                .build();
    }
}
