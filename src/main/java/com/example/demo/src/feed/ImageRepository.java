package com.example.demo.src.feed;

import com.example.demo.common.Constant.State;
import com.example.demo.src.feed.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByFeedIdAndState(Long feedId, State state);
}
