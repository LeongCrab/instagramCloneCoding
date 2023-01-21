package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserRes {
    private Long id;
    private String loginId;
    private String jwt;

    public PostUserRes(long id, String loginId) {
        this.id = id;
        this.loginId = loginId;
    }
}
