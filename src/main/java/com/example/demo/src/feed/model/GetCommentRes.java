package com.example.demo.src.feed.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"id", "loginId", "content"})
public class GetCommentRes {
    private Long Id;
    private String loginId;
    private String content;
}