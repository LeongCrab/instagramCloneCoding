package com.example.demo.src.feed.model;


import com.example.demo.common.Constant.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@AllArgsConstructor
public class PostReportReq {
    @Enumerated(EnumType.STRING)
    private ReportReason reportReason;
}
