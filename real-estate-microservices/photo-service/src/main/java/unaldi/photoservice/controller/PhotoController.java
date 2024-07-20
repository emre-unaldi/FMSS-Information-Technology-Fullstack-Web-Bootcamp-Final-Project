package unaldi.photoservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/photos")
@Tag(name="Photo Controller", description = "Photo Management")
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("/upload")
    @Operation(
            description = "Single photo upload",
            summary = "Upload a single photo",
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The photo file to upload",
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(
                                    implementation = SingleUploadRequest.class
                            )
                    )
            )
    )
    public ResponseEntity<DataResult<PhotoResponse>> singleUpload(@RequestPart("photo") MultipartFile photo) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.singleUpload(new SingleUploadRequest(photo)));
    }

    @PostMapping("/uploads")
    @Operation(
            description = "Multiple photo upload",
            summary = "Upload multiple photos",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The photo files to upload",
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(
                                    implementation = MultipleUploadRequest.class
                            )
                    )
            )
    )
    public ResponseEntity<DataResult<List<PhotoResponse>>> multipleUpload(@RequestPart("photos") MultipartFile[] photos) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.multipleUpload(new MultipleUploadRequest(photos)));
    }

    @GetMapping
    @Operation(
            description = "Find all photos",
            summary = "Find all",
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Photos Infos",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<List<PhotoResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.findAll());
    }

    @PostMapping("/findByIds")
    @Operation(
            description = "Find photos by ids",
            summary = "Find by ids",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Photo ids",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = PhotoIdsRequest.class
                            )
                    )
            )
    )
    public ResponseEntity<DataResult<List<PhotoResponse>>> findByPhotoIds(@RequestBody PhotoIdsRequest photoIdsRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.findByPhotoIds(photoIdsRequest));
    }

    @GetMapping("/{photoId}")
    @Operation(
            description = "Find photo by id",
            summary = "Find by id",
            parameters = {
                    @Parameter(
                            name = "photoId",
                            description = "Id of the photo to retrieve",
                            required = true,
                            example = "uuid",
                            schema = @Schema(type = "string")
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Photo id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<PhotoResponse>> findById(@PathVariable("photoId") String photoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.findById(photoId));
    }

    @DeleteMapping("/{photoId}")
    @Operation(
            description = "Delete photo by id",
            summary = "Delete a photo",
            parameters = {
                    @Parameter(
                            name = "photoId",
                            description = "Id of the photo to retrieve",
                            required = true,
                            example = "uuid",
                            schema = @Schema(type = "string")
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Photo id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<Result> deleteById(@PathVariable("photoId") String photoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(photoService.deleteById(photoId));
    }

    @GetMapping("/download/{photoId}")
    @Operation(
            description = "Download photo by id",
            summary = "Download by id",
            parameters = {
                    @Parameter(
                            name = "photoId",
                            description = "Id of the photo to retrieve",
                            required = true,
                            example = "uuid",
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Downloaded Photo",
                            content = @Content(
                                    mediaType = "application/octet-stream"
                            )
                    )
            }
    )
    public ResponseEntity<Resource> downloadById(@PathVariable("photoId") String photoId) {
        DownloadResponse downloaded = photoService.downloadById(photoId).getData();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloaded.getName() + "\"")
                .body(downloaded.getResource());
    }
}