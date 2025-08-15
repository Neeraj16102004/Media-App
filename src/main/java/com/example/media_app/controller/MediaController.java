package com.example.media_app.controller;

import com.example.media_app.entity.MediaAsset;
import com.example.media_app.repository.MediaAssetRepository;
import com.example.media_app.service.StreamTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaAssetRepository mediaAssetRepository;
    private final StreamTokenService streamTokenService;

    private final String MEDIA_FOLDER = "/Volumes/neeraj/projects/MEDIA_FOLDER"; // <- set your folder here

    // POST /media → upload a file
    @PostMapping
    public ResponseEntity<MediaAsset> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @AuthenticationPrincipal UserDetails user) throws IOException {

        // Save file to disk
        File mediaFolder = new File(MEDIA_FOLDER);
        if (!mediaFolder.exists()) mediaFolder.mkdirs();

        String filePath = MEDIA_FOLDER + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        // Save in DB
        MediaAsset media = MediaAsset.builder()
                .title(title)
                .type(type)
                .fileUrl(filePath)
                .createdAt(Instant.now())
                .build();

        mediaAssetRepository.save(media);

        return ResponseEntity.created(URI.create("/media/" + media.getId())).body(media);
    }

    // GET /media/{id}/stream-url → returns 10-min secure link
    @GetMapping("/{id}/stream-url")
    public ResponseEntity<String> getStreamUrl(@PathVariable Long id) {
        String token = streamTokenService.generateStreamToken(id);
        String streamUrl = "http://localhost:8080/stream?token=" + token;
        return ResponseEntity.ok(streamUrl);
    }
}


