package unaldi.photoservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unaldi.photoservice.entity.dto.request.MultipleUploadRequest;
import unaldi.photoservice.entity.dto.request.PhotoIdsRequest;
import unaldi.photoservice.entity.dto.request.SingleUploadRequest;
import unaldi.photoservice.entity.dto.response.DownloadResponse;
import unaldi.photoservice.entity.dto.response.PhotoResponse;
import unaldi.photoservice.service.PhotoService;
import unaldi.photoservice.utils.result.DataResult;
import unaldi.photoservice.utils.result.Result;

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
    public ResponseEntity<DataResult<PhotoResponse>> singleUpload(@RequestParam("photo") MultipartFile photo) throws Exception {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.singleUpload(new SingleUploadRequest(photo)));
    }

    @PostMapping("/uploads")
    public ResponseEntity<DataResult<List<PhotoResponse>>> multipleUpload(@RequestParam("photos") MultipartFile[] photos) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.multipleUpload(new MultipleUploadRequest(photos)));
    }

    @GetMapping
    public ResponseEntity<DataResult<List<PhotoResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.findAll());
    }

    @PostMapping("/findByIds")
    public ResponseEntity<DataResult<List<PhotoResponse>>> findByPhotoIds(@RequestBody PhotoIdsRequest photoIdsRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.findByPhotoIds(photoIdsRequest));
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<DataResult<PhotoResponse>> findById(@PathVariable("photoId") String photoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.findById(photoId));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Result> deleteById(@PathVariable("photoId") String photoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.deleteById(photoId));
    }

    @GetMapping("/download/{photoId}")
    public ResponseEntity<Resource> downloadById(@PathVariable("photoId") String photoId) {
        DownloadResponse downloaded = photoService.downloadById(photoId).getData();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloaded.getName() + "\"")
                .body(downloaded.getResource());
    }
}
