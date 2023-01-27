package com.example.demo.src.user;

import com.example.demo.src.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.example.demo.common.Constant.*;

public interface UserRepository extends JpaRepository<User, Long>, RevisionRepository<User, Long, Integer> {

    Optional<User> findByIdAndState(Long id, State state);
    Optional<User> findById(Long id);
    Optional<User> findByLoginIdAndState(String loginId, State state);
    Optional<User> findByLoginId(String loginId);
    Page<User> findAll(Pageable pageable);
    @Query("select u " +
            "from User u " +
            "where (:userName is null or u.name = :userName) " +
            "and (:userId is null or u.id = :userId) " +
            "and (:userState is null or u.userState = :userState)" +
            "and (:createdAt is null or u.createdAt like concat(:createdAt,'%'))" )
    Page<User> findUsers(@Param("userName") String userName, @Param("userId") Long userId, @Param("userState") UserState userState, @Param("createdAt") String createdAt, Pageable pageable);

}
