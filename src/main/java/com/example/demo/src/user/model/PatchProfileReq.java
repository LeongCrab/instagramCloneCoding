package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter

public class PatchProfileReq {
    private String profileImage;
    @Size(max=200, message = "[profile] 200자까지 입력 가능합니다.")
    private String profileText;
}
