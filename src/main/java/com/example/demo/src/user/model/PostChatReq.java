package com.example.demo.src.user.model;

import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class PostChatReq {
    @Size(max=200, message = "채팅은 최대 200자까지 보낼 수 있습니다.")
    private String text;

}
