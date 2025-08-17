package com.example.media_app.controller;

import com.example.media_app.service.MediaAnalyticsService;
import jakarta.persistence.Cacheable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaAnalyticsController {

    private final MediaAnalyticsService analyticsService;

    // Log a view
    @PostMapping("/{id}/view")
    public ResponseEntity<String> logView(@PathVariable Long id, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        analyticsService.logView(id, ip);
        return ResponseEntity.ok("View logged successfully");
    }

    // Get analytics
    @GetMapping("/{id}/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(@PathVariable Long id) {
        return ResponseEntity.ok(analyticsService.getAnalytics(id));
    }
}

