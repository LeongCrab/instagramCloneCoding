package com.example.demo.src.post;

import com.example.demo.src.post.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static com.example.demo.common.entity.BaseEntity.*;


public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPostIdAndState(Long postId, State state);
}
