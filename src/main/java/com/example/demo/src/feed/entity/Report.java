package com.example.demo.src.feed.entity;


import com.example.demo.common.Constant.ReportReason;
import com.example.demo.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "REPORT")
public class Report extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private ReportReason reportReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Comment comment;

    @Builder
    public Report(long id, ReportReason reportReason, Feed feed, Comment comment){
        this.id = id;
        this.reportReason = reportReason;
        this.feed = feed;
        this.comment = comment;
    }
}
