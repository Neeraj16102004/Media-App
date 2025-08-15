package com.example.media_app.dto;

import lombok.Data;

@Data
public class MediaRequest {
    private String title;
    private String type; // video / audio
    private String fileUrl;
}

