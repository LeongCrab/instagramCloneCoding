package com.example.demo.src.feed.model;


import com.example.demo.common.Constant.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReportReq {
    private ReportReason reportReason;
}
