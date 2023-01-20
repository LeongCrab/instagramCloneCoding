package com.example.demo.src.post;

import com.example.demo.src.post.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByUserIdAndPostId(long userId, long postId);
    int countByPostIdAndState(long postId, State state);
}
