package com.example.demo.src.admin.model;

import com.example.demo.src.feed.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReportRes {
    private Long id;
    private Long feedId;
    private String type;
    private String loginId;
    private String reportReason;
    private String createAt;

    public GetReportRes(Report report) {
        this.id = report.getId();
        this.type = report.getType();
        this.feedId = report.getFeed().getId();
        this.loginId = report.getFeed().getUser().getLoginId();
        this.reportReason = report.getReportReason().toString();
        this.createAt = report.getCreatedAt().toString();
    }
}

