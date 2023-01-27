package com.example.demo.src.history;


import com.example.demo.src.history.entity.CommentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentHistoryRepository extends JpaRepository<CommentHistory, Long> {
    @Query("select h " +
            "from CommentHistory h " +
            "where (date_format(h.createdAt, '%Y-%m-%d %h:%m:%s') >= :start or :start is null) and (date_format(h.createdAt, '%Y-%m-%d %h:%m:%s') <= :end or :end is null)")
    Page<CommentHistory> findAllByPeriod(@Param("start") String start, @Param("end") String end, Pageable pageable);
}
