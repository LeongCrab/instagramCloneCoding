package com.example.demo.src.feed;

import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.src.feed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByUserIdAndIdAndState(long userId, long id, State state);
    int countByFeedIdAndState(long feedId, State state);

    List<Comment> findAllByIdAndState(long id, State state);
}
