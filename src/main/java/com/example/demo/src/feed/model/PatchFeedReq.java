package com.example.demo.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class PatchFeedReq {
    @Size(max=2200, message = "[feed] 내용은 2,200자까지 입력 가능합니다.")
    private String content;
}