package com.example.demo.src.history;


import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.history.entity.FeedHistory;
import com.example.demo.utils.BeanUtil;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class FeedEntityListener {
    @PostPersist
    @PostUpdate
    public void postPersistAndPostUpdate(Object o) {
        FeedHistoryRepository feedHistoryRepository = BeanUtil.getBean(FeedHistoryRepository.class);

        Feed feed = (Feed) o;

        FeedHistory feedHistory = FeedHistory.builder()
                .feedId(feed.getId())
                .content(feed.getContent())
                .user(feed.getUser())
                .feedState(feed.getFeedState())
                .build();

        feedHistoryRepository.save(feedHistory);
    }
}
