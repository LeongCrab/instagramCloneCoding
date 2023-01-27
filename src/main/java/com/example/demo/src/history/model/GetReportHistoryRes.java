package com.example.demo.src.history.model;

import com.example.demo.src.feed.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReportHistoryRes {
    private Long id;
    private String CreatedAt;

    public GetReportHistoryRes(Report report) {
        this.id = report.getId();
        this.CreatedAt = report.getCreatedAt().toString();
    }
}
