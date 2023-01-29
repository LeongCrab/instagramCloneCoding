package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetSocialOAuthRes {

    private String jwtToken;
    private Long userId;
    private String accessToken;
    private String tokenType;
}
