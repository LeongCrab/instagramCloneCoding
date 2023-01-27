package com.example.demo.src.history.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetHistoryReq {
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-([012][0-9]|3[01]) ([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])", message= "올바른 날짜를 입력해 주세요. 날짜 시간 형식은 'YYYY-MM-DD hh:mm:ss' 입니다.")
    private String start;
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-([012][0-9]|3[01]) ([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])", message= "올바른 날짜를 입력해 주세요. 날짜 시간 형식은 'YYYY-MM-DD hh:mm:ss' 입니다.")
    private String end;
}
