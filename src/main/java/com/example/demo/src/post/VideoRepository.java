package com.example.demo.src.post;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.post.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.example.demo.common.entity.BaseEntity.*;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAllByPostIdAndState(Long postId, BaseEntity.State state);
}
