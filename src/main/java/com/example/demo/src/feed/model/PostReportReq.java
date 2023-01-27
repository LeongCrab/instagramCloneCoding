package com.example.demo.src.feed.model;


import com.example.demo.common.Constant.ReportReason;
import com.example.demo.utils.ValidEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReportReq {
    @ValidEnum(enumClass = ReportReason.class)
    private ReportReason reportReason;
}
