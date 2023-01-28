package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserReq {
    @NotBlank(message= "변경할 비밀번호를 입력하세요")
    @Size(min=6, max=20, message = "비밀번호는 6~20자로 입력해주세요")
    private String password;
}
