package unaldi.photoservice.service;

import unaldi.photoservice.entity.dto.request.MultipleUploadRequest;
import unaldi.photoservice.entity.dto.request.PhotoIdsRequest;
import unaldi.photoservice.entity.dto.request.SingleUploadRequest;
import unaldi.photoservice.entity.dto.response.DownloadResponse;
import unaldi.photoservice.entity.dto.response.PhotoResponse;
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
public interface PhotoService {

    DataResult<PhotoResponse> singleUpload(SingleUploadRequest singleUploadRequest) throws Exception;
    DataResult<List<PhotoResponse>> multipleUpload(MultipleUploadRequest multipleUploadRequest);
    DataResult<List<PhotoResponse>> findAll();
    DataResult<List<PhotoResponse>> findByPhotoIds(PhotoIdsRequest photoIdsRequest);
    DataResult<PhotoResponse> findById(String photoId);
    DataResult<DownloadResponse> downloadById(String photoId);
    Result deleteById(String photoId);

}
