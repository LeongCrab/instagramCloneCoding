package com.example.demo.src.user.model;

import com.example.demo.common.Constant.LoginType;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    @NotBlank(message= "휴대폰 번호를 입력하세요")
    @Pattern(regexp = "^[0-9]+$", message = "휴대폰 번호는 숫자만 입력 가능합니다")
    @Size(max=20, message = "휴대폰 번호는 최대 20자까지 입력 가능합니다.")
    private String phone;
    @NotBlank(message= "이름을 입력하세요")
    @Size(max=20, message = "이름은 최대 20자까지 입력 가능합니다.")
    private String name;
    @NotBlank(message= "로그인 아이디를 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9._]+$", message= "아이디는 알파벳과 '.', '_'만 입력 가능합니다.")
    @Size(max=20, message = "로그인 아이디는 최대 20자까지 입력 가능합니다.")
    private String loginId;
    @NotBlank(message= "비밀번호를 입력하세요")
    @Size(min=6, max=20, message = "비밀번호는 6~20자 입력 가능합니다.")
    private String password;
    @NotBlank(message = "생일을 입력하세요")
    @Pattern(regexp = "(0[1-9]|1[012])([012][0-9]|3[01])", message= "생일 형식은 'MMDD'입니다")
    private String birthday;

    @Size(min=4, max=4, message = "해는 4자리로 입력해주세요.")
    private String birthYear;

    //가입 시 자동으로 개인 정보 만료 날짜 1년 추가
    private LocalDate privacyExpiredAt = LocalDate.now().plusYears(1);

    protected LoginType loginType;

    public User toEntity() {
        return User.builder()
                .phone(this.phone)
                .name(this.name)
                .loginId(this.loginId)
                .password(this.password)
                .birthday(this.birthday)
                .birthYear(this.birthYear)
                .loginType(this.loginType)
                .privacyExpiredAt(this.privacyExpiredAt)
                .build();
    }
}
