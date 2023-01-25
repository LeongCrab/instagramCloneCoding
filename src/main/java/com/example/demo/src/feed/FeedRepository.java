package com.example.demo.src.feed;

import com.example.demo.src.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findAllByState(State state, Pageable pageable);
    Page<Feed> findByUserIdAndState(Long userId, State state, Pageable pageable);

    Optional<Feed> findByIdAndState(Long id, State state);
    int countByUserIdAndState(Long userId, State state);
    Page<Feed> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"user"})
    @Query("select f " +
            "from Feed f " +
            "where (:loginId is null or f.user.loginId = :loginId) " +
            "and (:state is null or f.state = :state)" +
            "and (:createdAt is null or f.createdAt like concat(:createdAt,'%'))" )
    Page<Feed> findFeeds(@Param("loginId") String loginId, @Param("state") State state, @Param("createdAt") String createdAt, Pageable pageable);

    Optional<Feed> findById(Long id);
}
