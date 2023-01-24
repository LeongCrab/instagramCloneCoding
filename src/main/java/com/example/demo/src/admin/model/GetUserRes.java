package com.example.demo.src.admin.model;


import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserRes {
    private Long id;
    private String loginId;
    private String createAt;
    public GetUserRes(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.createAt = user.getCreatedAt().toString();
    }
}
