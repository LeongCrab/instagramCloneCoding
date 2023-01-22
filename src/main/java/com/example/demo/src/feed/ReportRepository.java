package com.example.demo.src.feed;

import com.example.demo.common.Constant.ReportReason;
import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.src.feed.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    int countByFeedIdAndReportReasonAndState(long feedId, ReportReason reportReason, State state);
}
