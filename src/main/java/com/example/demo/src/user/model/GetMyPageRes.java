package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetMyPageRes {
    private String loginId;
    private int feeds;
    private int followers;
    private int followings;
    private String profileText;
    private String profileImage;
}