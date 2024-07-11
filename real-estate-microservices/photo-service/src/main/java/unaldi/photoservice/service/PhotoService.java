package unaldi.photoservice.service;

import unaldi.photoservice.entity.dto.request.MultipleUploadRequest;
import unaldi.photoservice.entity.dto.request.SingleUploadRequest;
import unaldi.photoservice.entity.dto.response.DownloadResponse;
import unaldi.photoservice.entity.dto.response.PhotoResponse;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
public interface PhotoService {

    PhotoResponse singleUpload(SingleUploadRequest singleUploadRequest) throws Exception;
    List<PhotoResponse> multipleUpload(MultipleUploadRequest multipleUploadRequest);
    List<PhotoResponse> findAll();
    PhotoResponse findById(String photoId);
    DownloadResponse downloadById(String photoId);
    Boolean deleteById(String photoId);

}
