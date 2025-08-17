package com.example.media_app.repository;

import com.example.media_app.entity.MediaViewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface MediaViewLogRepository extends JpaRepository<MediaViewLog, Long> {
    List<MediaViewLog> findByMediaId(Long mediaId);

    @Query("SELECT COUNT(DISTINCT m.viewedByIp) FROM MediaViewLog m WHERE m.media.id = :mediaId")
    long countDistinctByMediaId(Long mediaId);

    @Query("SELECT FUNCTION('DATE', m.timestamp), COUNT(m) " +
            "FROM MediaViewLog m WHERE m.media.id = :mediaId " +
            "GROUP BY FUNCTION('DATE', m.timestamp)")
    List<Object[]> countViewsPerDay(Long mediaId);
}

