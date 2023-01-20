package com.example.demo.src.post;

import com.example.demo.src.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import static com.example.demo.common.entity.BaseEntity.*;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByState(State state, Pageable pageable);
    Page<Post> findByUserIdAndState(Long userId, State state, Pageable pageable);

}
