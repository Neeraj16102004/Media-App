package com.example.media_app.controller;

import com.example.media_app.entity.MediaAsset;
import com.example.media_app.repository.MediaAssetRepository;
import com.example.media_app.service.StreamTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequiredArgsConstructor
public class StreamController {

    private final StreamTokenService streamTokenService;
    private final MediaAssetRepository mediaAssetRepository;

    @GetMapping("/stream")
    public void streamMedia(@RequestParam String token, HttpServletResponse response) throws IOException {

        Long mediaId = streamTokenService.validateStreamToken(token);
        if (mediaId == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        MediaAsset media = mediaAssetRepository.findById(mediaId).orElse(null);
        if (media == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Media not found");
            return;
        }

        File file = new File(media.getFileUrl());
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("File not found");
            return;
        }

        // Set correct content type
        response.setContentType(media.getType().equalsIgnoreCase("video") ? "video/mp4" : "audio/mpeg");
        response.setHeader("Content-Length", String.valueOf(file.length()));

        // Stream the file
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        }
    }
}




