package com.example.demo.src.user.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLoginReq {
    @NotBlank(message= "Insert user id")
    @Pattern(regexp = "^[a-zA-Z0-9._]+$", message= "[user id] Invalid format")
    @Size(max=20, message = "[login id] maximum 20 characters")
    private String loginId;
    @NotBlank(message= "Insert password")
    @Size(min=6, max=20, message = "[password] 6 ~ 20 characters")
    private String password;
}
