package com.example.demo.src.admin.model;

import com.example.demo.common.Constant.UserState;
import com.example.demo.utils.ValidEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserReq {
    private Long userId;
    private String userName;
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-([012][0-9]|3[01])", message= "올바른 날짜를 입력해 주세요. 날짜 형식은 'YYYY-MM-DD' 입니다.")
    private String createdAt;
    @ValidEnum(enumClass = UserState.class)
    private UserState userState;
}
