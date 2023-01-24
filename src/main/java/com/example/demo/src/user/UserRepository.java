package com.example.demo.src.user;

import com.example.demo.src.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndState(Long id, State state);
    Optional<User> findById(Long id);
    Optional<User> findByLoginIdAndState(String loginId, State state);
    Optional<User> findByLoginId(String loginId);

    @Query("select u " +
            "from User u " +
            "where (:userName is null or u.name = :userName) " +
            "and (:userId is null or u.id = :userId) " +
            "and (:state is null or u.state = :state)" +
            "and (:createdAt is null or u.createdAt like concat(:createdAt,'%'))" )
    Page<User> findAllUsers(@Param("userName") String userName, @Param("userId") Long userId, @Param("state") State state, @Param("createdAt") String createdAt, Pageable pageable);

}
