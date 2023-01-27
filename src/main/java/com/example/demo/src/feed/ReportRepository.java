package com.example.demo.src.feed;


import com.example.demo.common.Constant.State;
import com.example.demo.src.feed.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    int countByFeedIdAndState(long feedId, State state);
    int countByCommentIdAndState(long commentId, State state);
    Page<Report> findAllByState(State state, Pageable pageable);
    Optional<Report> findByIdAndState(Long id, State state);
    @Query("select r " +
            "from Report r " +
            "where (date_format(r.createdAt, '%Y-%m-%d %h:%m:%s') >= :start or :start is null) and (date_format(r.createdAt, '%Y-%m-%d %h:%m:%s') <= :end or :end is null)")
    Page<Report> findAllByPeriod(@Param("start") String start, @Param("end") String end, Pageable pageable);
}
