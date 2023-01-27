package com.example.demo.src.history.model;

import com.example.demo.src.history.entity.FeedHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFeedHistoryRes {
    private Long id;
    private Long feedId;
    private String CreatedAt;

    public GetFeedHistoryRes(FeedHistory feedHistory) {
        this.id = feedHistory.getId();
        this.feedId = feedHistory.getFeedId();
        this.CreatedAt = feedHistory.getCreatedAt().toString();
    }
}