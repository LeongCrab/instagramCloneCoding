package com.example.demo.src.feed;

import com.example.demo.src.feed.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
