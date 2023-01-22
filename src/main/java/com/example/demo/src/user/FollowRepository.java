package com.example.demo.src.user;


import com.example.demo.src.user.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(long followerId, long followingId);
    int countByFollowerIdAndState(long followerId, State state);
    int countByFollowingIdAndState(long followingId, State state);
}