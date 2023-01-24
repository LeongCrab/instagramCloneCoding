package com.example.demo.src.admin;

import com.example.demo.src.admin.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {

}
