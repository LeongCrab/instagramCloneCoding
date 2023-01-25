package com.example.demo.src.admin;

import com.example.demo.common.Constant;
import com.example.demo.src.admin.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogRepository extends JpaRepository<Log, Long> {
    Optional<Log> findFirstByDataTypeAndUserIdOrderByCreatedAtDesc(Constant.DataType dataType, Long userId);
}