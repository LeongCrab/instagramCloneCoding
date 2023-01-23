package com.example.demo.src.user;

import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.src.user.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ChatRepository extends JpaRepository<Chat, Long> {
    @EntityGraph(attributePaths = {"sender", "receiver"})
    @Query("select c from Chat c where ((c.sender.id = :senderId and c.receiver.id = :receiverId) or (c.sender.id = :receiverId and c.receiver.id = :senderId)) and c.state = :state")
    Page<Chat> findChat(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId, @Param("state") State ACTIVE, Pageable pageable);
}
