package com.example.demo.src.feed;


import com.example.demo.common.Constant.State;
import com.example.demo.src.feed.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    int countByFeedIdAndState(long feedId, State state);

    Page<Report> findAllByState(State state, Pageable pageable);
}
