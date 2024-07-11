package unaldi.photoservice.service.Impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import unaldi.photoservice.entity.Photo;
import unaldi.photoservice.entity.dto.request.MultipleUploadRequest;
import unaldi.photoservice.entity.dto.request.SingleUploadRequest;
import unaldi.photoservice.entity.dto.response.DownloadResponse;
import unaldi.photoservice.entity.dto.response.PhotoResponse;
import unaldi.photoservice.repository.PhotoRepository;
import unaldi.photoservice.service.PhotoService;
import unaldi.photoservice.service.mapper.PhotoMapper;
import unaldi.photoservice.utils.constants.Messages;
import unaldi.photoservice.utils.result.DataResult;
import unaldi.photoservice.utils.result.Result;
import unaldi.photoservice.utils.result.SuccessDataResult;
import unaldi.photoservice.utils.result.SuccessResult;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public DataResult<PhotoResponse> singleUpload(SingleUploadRequest singleUploadRequest) throws Exception {
        MultipartFile uploadPhoto = singleUploadRequest.getPhoto();

        if (uploadPhoto.getOriginalFilename() == null || uploadPhoto.getOriginalFilename().trim().isEmpty()) {
            throw new Exception("Photo name is null or empty");
        }

        String photoName = StringUtils.cleanPath(uploadPhoto.getOriginalFilename());
        String contentType = uploadPhoto.getContentType();

        if (!isImageFormat(contentType)) {
            throw new Exception("Invalid file format. Only image files are allowed");
        }

        try {

            if (photoName.contains("..")) {
                throw new Exception("Photo name contains invalid path sequence " + photoName);
            }

            if (photoName.getBytes().length > (1024 * 1024)) {
                throw new Exception("Photo size exceeds maximum limit");
            }

            if (photoRepository.existsByPhotoName(photoName)) {
                throw new FileAlreadyExistsException("A Photo of that name already exists : " + photoName);
            }

            byte[] sourceData = uploadPhoto.getBytes();
            Photo photoDto = PhotoMapper.INSTANCE.uploadRequestToPhoto(photoName, contentType, sourceData);
            Photo photo = photoRepository.save(photoDto);

            return new SuccessDataResult<>(
                    PhotoMapper.INSTANCE.photoToPhotoResponse(photo),
                    Messages.PHOTO_SINGLE_UPLOAD);

        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(uploadPhoto.getSize());
        } catch (Exception e) {
            throw new Exception("Could not upload the file: " + photoName + "!"); // HttpStatus.EXPECTATION_FAILED
        }
    }

    @Override
    public DataResult<List<PhotoResponse>> multipleUpload(MultipleUploadRequest multipleUploadRequest) {
        List<PhotoResponse> photos = new ArrayList<>();

        Arrays
                .stream(multipleUploadRequest.getPhotos())
                .forEach(photo -> {
                    try {
                        SingleUploadRequest singleUploadRequest = new SingleUploadRequest(photo);
                        photos.add(singleUpload(singleUploadRequest).getData());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return new SuccessDataResult<>(photos, Messages.PHOTO_MULTIPLE_UPLOAD);
    }

    @Override
    public DataResult<List<PhotoResponse>> findAll() {
        List<Photo> photos = photoRepository.findAll();

        return new SuccessDataResult<>(
                PhotoMapper.INSTANCE.photosToPhotoResponses(photos),
                Messages.PHOTOS_LISTED);
    }

    @Override
    public DataResult<PhotoResponse> findById(String photoId) {
        Optional<Photo> photo = photoRepository.findById(photoId);

        if (photo.isEmpty()) {
            throw new RuntimeException("Could not find the photo with id : " + photoId);
        }

        return new SuccessDataResult<>(
                PhotoMapper.INSTANCE.photoToPhotoResponse(photo.get()),
                Messages.PHOTO_FOUND
        );
    }

    @Override
    public DataResult<DownloadResponse> downloadById(String photoId) {
        try {
            Optional<Photo> photo = photoRepository.findById(photoId);

            if (photo.isEmpty()) {
                throw new RuntimeException("Could not find the photo with id : " + photoId);
            }

            byte[] data = photo.get().getSourceData();
            ByteArrayResource resource = new ByteArrayResource(data);

            if (resource.exists() || resource.isReadable()) {

                return new SuccessDataResult<>(
                        PhotoMapper.INSTANCE.photoToDownloadResponse(photo.get(), resource)
                );
            } else {
                throw new RuntimeException("Could not read the photo with id : " + photoId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public Result deleteById(String photoId) {
        Optional<Photo> photo = photoRepository.findById(photoId);

        if (photo.isEmpty()) {
            throw new RuntimeException("Could not find the photo with id : " + photoId);
        }

        photoRepository.deleteById(photo.get().getId());

        return new SuccessResult(Messages.PHOTO_DELETED);
    }

    private boolean isImageFormat(String contentType) {
        List<String> allowedImageFormats = Arrays.asList(
                "image/jpg",
                "image/jpeg",
                "image/png",
                "image/gif",
                "image/bmp",
                "image/webp",
                "image/tiff",
                "image/svg+xml"
        );

        return allowedImageFormats.contains(contentType);
    }

}
