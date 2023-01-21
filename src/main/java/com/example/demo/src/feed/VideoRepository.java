package com.example.demo.src.feed;

import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.src.feed.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAllByFeedIdAndState(Long feedId, State state);
}
