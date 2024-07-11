package unaldi.photoservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.photoservice.entity.dto.request.MultipleUploadRequest;
import unaldi.photoservice.entity.dto.request.SingleUploadRequest;
import unaldi.photoservice.entity.dto.response.DownloadResponse;
import unaldi.photoservice.entity.dto.response.PhotoResponse;
import unaldi.photoservice.service.PhotoService;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
@RestController
@RequestMapping("/api/v1/photos")
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<PhotoResponse> singleUpload(@RequestParam("photo") SingleUploadRequest photo) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.singleUpload(photo));
    }

    @PostMapping("/uploads")
    public ResponseEntity<List<PhotoResponse>> multipleUpload(@RequestParam("photos") MultipleUploadRequest photos) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.multipleUpload(photos));
    }

    @GetMapping
    public ResponseEntity<List<PhotoResponse>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.findAll());
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoResponse> findById(@PathVariable("photoId") String photoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.findById(photoId));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("photoId") String photoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.deleteById(photoId));
    }

    @GetMapping("/download/{photoId}")
    public ResponseEntity<Resource> downloadById(@PathVariable("photoId") String photoId) {
        DownloadResponse downloaded = photoService.downloadById(photoId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloaded.getName() + "\"")
                .body(downloaded.getResource());
    }
}
