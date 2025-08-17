package com.example.media_app.service;

import com.example.media_app.entity.MediaAsset;
import com.example.media_app.entity.MediaViewLog;
import com.example.media_app.repository.MediaAssetRepository;
import com.example.media_app.repository.MediaViewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MediaAnalyticsService {

    private final MediaAssetRepository mediaAssetRepository;
    private final MediaViewLogRepository viewLogRepository;

    public void logView(Long mediaId, String ip) {
        MediaAsset media = mediaAssetRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        MediaViewLog log = MediaViewLog.builder()
                .media(media)
                .viewedByIp(ip)
                .timestamp(Instant.now())
                .build();

        viewLogRepository.save(log);
    }

    @Cacheable(value = "mediaAnalytics", key = "#mediaId")  // Redis Caching
    public Map<String, Object> getAnalytics(Long mediaId) {
        if (!mediaAssetRepository.existsById(mediaId)) {
            throw new RuntimeException("Media not found");
        }

        long totalViews = viewLogRepository.findByMediaId(mediaId).size();
        long uniqueIps = viewLogRepository.countDistinctByMediaId(mediaId);

        Map<String, Integer> viewsPerDay = new HashMap<>();
        List<Object[]> results = viewLogRepository.countViewsPerDay(mediaId);

        for (Object[] row : results) {
            String date = row[0].toString();
            Integer count = ((Number) row[1]).intValue();
            viewsPerDay.put(date, count);
        }

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("total_views", totalViews);
        analytics.put("unique_ips", uniqueIps);
        analytics.put("views_per_day", viewsPerDay);

        return analytics;
    }

    private final CacheManager cacheManager;

    private static final String ANALYTICS_CACHE = "mediaAnalytics";
    public void evictCache(Long mediaId) {
        Objects.requireNonNull(cacheManager.getCache(ANALYTICS_CACHE)).evict(mediaId);
    }
}


