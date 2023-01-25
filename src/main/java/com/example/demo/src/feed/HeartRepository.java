package com.example.demo.src.feed;


import com.example.demo.common.Constant.State;
import com.example.demo.src.feed.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByUserIdAndFeedId(long userId, long feedId);
    int countByFeedIdAndState(long feedId, State state);
}
