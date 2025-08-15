package com.example.media_app.repository;


import com.example.media_app.entity.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
    MediaAsset getMediaById(Long mediaId);
}


