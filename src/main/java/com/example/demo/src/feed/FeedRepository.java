package com.example.demo.src.feed;

import com.example.demo.src.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findAllByState(State state, Pageable pageable);
    Page<Feed> findByUserIdAndState(Long userId, State state, Pageable pageable);

    Optional<Feed> findByIdAndState(Long id, State state);
    int countByUserIdAndState(long userId, State state);

}
