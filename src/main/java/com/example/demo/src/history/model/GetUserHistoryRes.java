package com.example.demo.src.history.model;

import com.example.demo.src.history.entity.UserHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserHistoryRes {
    private Long id;
    private Long userId;
    private String CreatedAt;

    public GetUserHistoryRes(UserHistory userHistory) {
        this.id = userHistory.getId();
        this.userId = userHistory.getUserId();
        this.CreatedAt = userHistory.getCreatedAt().toString();
    }
}
