package unaldi.photoservice.service.Impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import unaldi.photoservice.entity.Photo;
import unaldi.photoservice.entity.dto.request.MultipleUploadRequest;
import unaldi.photoservice.entity.dto.request.PhotoIdsRequest;
import unaldi.photoservice.entity.dto.request.SingleUploadRequest;
import unaldi.photoservice.entity.dto.response.DownloadResponse;
import unaldi.photoservice.entity.dto.response.PhotoResponse;
import unaldi.photoservice.repository.PhotoRepository;
import unaldi.photoservice.service.PhotoService;
import unaldi.photoservice.service.mapper.PhotoMapper;
import unaldi.photoservice.utils.constants.Caches;
import unaldi.photoservice.utils.constants.ExceptionMessages;
import unaldi.photoservice.utils.constants.Messages;
import unaldi.photoservice.utils.exception.*;
import unaldi.photoservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.photoservice.utils.rabbitMQ.enums.HttpRequestMethod;
import unaldi.photoservice.utils.rabbitMQ.enums.LogType;
import unaldi.photoservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.photoservice.utils.result.DataResult;
import unaldi.photoservice.utils.result.Result;
import unaldi.photoservice.utils.result.SuccessDataResult;
import unaldi.photoservice.utils.result.SuccessResult;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private final LogProducer logProducer;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, LogProducer logProducer) {
        this.photoRepository = photoRepository;
        this.logProducer = logProducer;
    }

    @CacheEvict(value = Caches.PHOTOS_CACHE, allEntries = true, condition = "#result.success != false")
    @Override
    public DataResult<PhotoResponse> singleUpload(SingleUploadRequest request) {
        MultipartFile uploadPhoto = request.getPhoto();

        if (uploadPhoto.getOriginalFilename() == null || uploadPhoto.getOriginalFilename().trim().isEmpty()) {
            throw new PhotoNameEmptyException(ExceptionMessages.PHOTO_NAME_EMPTY);
        }

        String photoName = StringUtils.cleanPath(uploadPhoto.getOriginalFilename());
        String contentType = uploadPhoto.getContentType();

        if (!isImageFormat(contentType)) {
            throw new InvalidPhotoFormatException(ExceptionMessages.INVALID_PHOTO_FORMAT);
        }

        if (photoName.contains("..")) {
            throw new InvalidPathSequenceException(ExceptionMessages.INVALID_PHOTO_NAME_PATH_SEQUENCE);
        }

        if (photoName.getBytes().length > (1024 * 1024)) {
            throw new MaxUploadSizeExceededException(uploadPhoto.getSize());
        }

        if (photoRepository.existsByPhotoName(photoName)) {
            throw new PhotoAlreadyExistsException(ExceptionMessages.PHOTO_NAME_ALREADY_EXISTS + photoName);
        }

        byte[] sourceData;
        try {
            sourceData = uploadPhoto.getBytes();
        } catch (IOException exception) {
            throw new PhotoNotReadException(ExceptionMessages.PHOTO_NOT_READ);
        }
        Photo photoDto = PhotoMapper.INSTANCE.uploadRequestToPhoto(photoName, contentType, sourceData);
        Photo photo = photoRepository.save(photoDto);

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.POST, Messages.PHOTO_SINGLE_UPLOAD));

        return new SuccessDataResult<>(
                PhotoMapper.INSTANCE.photoToPhotoResponse(photo),
                Messages.PHOTO_SINGLE_UPLOAD);
    }

    @CacheEvict(value = Caches.PHOTOS_CACHE, allEntries = true, condition = "#result.success != false")
    @Override
    public DataResult<List<PhotoResponse>> multipleUpload(MultipleUploadRequest request) {
        List<PhotoResponse> photos = new ArrayList<>();

        Arrays.stream(request.getPhotos())
                .forEach(photo -> photos.add(singleUpload(PhotoMapper.INSTANCE.multipartFileToSingleUploadRequest(photo)).getData()));

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.POST, Messages.PHOTO_MULTIPLE_UPLOAD));

        return new SuccessDataResult<>(photos, Messages.PHOTO_MULTIPLE_UPLOAD);
    }

    @Cacheable(value = Caches.PHOTOS_CACHE, key = "'all'", unless = "#result.success != true")
    @Override
    public DataResult<List<PhotoResponse>> findAll() {
        List<Photo> photos = photoRepository.findAll();

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.GET, Messages.PHOTOS_LISTED));

        return new SuccessDataResult<>(
                PhotoMapper.INSTANCE.photosToPhotoResponses(photos),
                Messages.PHOTOS_LISTED);
    }

    @Cacheable(value = Caches.PHOTOS_BY_IDS_CACHE, key = "#request.getPhotoIds()", unless = "#result.success != true")
    @Transactional
    @Override
    public DataResult<List<PhotoResponse>> findByPhotoIds(PhotoIdsRequest request) {
        List<Photo> photos = photoRepository.findByPhotoIds(request.getPhotoIds());

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.POST, Messages.PHOTO_IDS_LISTED));

        return new SuccessDataResult<>(
                PhotoMapper.INSTANCE.photosToPhotoResponses(photos),
                Messages.PHOTO_IDS_LISTED
        );
    }

    @Cacheable(value = Caches.PHOTO_CACHE, key = "#photoId", unless = "#result.success != true")
    @Override
    public DataResult<PhotoResponse> findById(String photoId) {
        Optional<Photo> photo = photoRepository.findById(photoId);

        if (photo.isEmpty()) {
            throw new PhotoNotFoundException(ExceptionMessages.PHOTO_NOT_FOUND);
        }

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.GET, Messages.PHOTO_FOUND));

        return new SuccessDataResult<>(
                PhotoMapper.INSTANCE.photoToPhotoResponse(photo.get()),
                Messages.PHOTO_FOUND
        );
    }

    @Override
    public DataResult<DownloadResponse> downloadById(String photoId) {
        Optional<Photo> photo = photoRepository.findById(photoId);

        if (photo.isEmpty()) {
            throw new PhotoNotFoundException(ExceptionMessages.PHOTO_NOT_FOUND);
        }

        byte[] data = photo.get().getSourceData();
        ByteArrayResource resource = new ByteArrayResource(data);

        if (resource.exists() && resource.isReadable()) {
            logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.GET, Messages.PHOTO_DOWNLOADED));

            return new SuccessDataResult<>(PhotoMapper.INSTANCE.photoToDownloadResponse(photo.get(), resource));
        } else {
            throw new PhotoNotReadException(ExceptionMessages.PHOTO_NOT_READ);
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(value = Caches.PHOTOS_CACHE, allEntries = true, condition = "#result.success != false"),
                    @CacheEvict(value = Caches.PHOTOS_BY_IDS_CACHE, allEntries = true, condition = "#result.success != false"),
                    @CacheEvict(value = Caches.PHOTO_CACHE, key = "#photoId", condition = "#result.success != false"),
            }
    )
    @Transactional
    @Override
    public Result deleteById(String photoId) {
        Optional<Photo> photo = photoRepository.findById(photoId);

        if (photo.isEmpty()) {
            throw new PhotoNotFoundException(ExceptionMessages.PHOTO_NOT_FOUND);
        }

        photoRepository.deleteById(photo.get().getId());
        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.DELETE, Messages.PHOTO_DELETED));

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

    private LogDTO prepareLogDTO(HttpRequestMethod httpRequestMethod, String message) {
        return LogDTO
                .builder()
                .serviceName("user-service")
                .httpRequestMethod(httpRequestMethod)
                .logType(LogType.INFO)
                .message(message)
                .timestamp(LocalDateTime.now())
                .exception(null)
                .build();
    }

}
