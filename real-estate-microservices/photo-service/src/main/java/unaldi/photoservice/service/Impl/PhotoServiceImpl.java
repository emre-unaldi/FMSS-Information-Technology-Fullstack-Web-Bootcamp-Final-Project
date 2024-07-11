package unaldi.photoservice.service.Impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import unaldi.photoservice.entity.Photo;
import unaldi.photoservice.entity.dto.request.MultipleUploadRequest;
import unaldi.photoservice.entity.dto.request.SingleUploadRequest;
import unaldi.photoservice.entity.dto.response.DownloadResponse;
import unaldi.photoservice.entity.dto.response.PhotoResponse;
import unaldi.photoservice.repository.PhotoRepository;
import unaldi.photoservice.service.PhotoService;

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
    public PhotoResponse singleUpload(SingleUploadRequest singleUploadRequest) throws Exception {
        MultipartFile photo = singleUploadRequest.getPhoto();

        if (photo.getOriginalFilename() == null || photo.getOriginalFilename().trim().isEmpty()) {
            throw new Exception("Photo name is null or empty");
        }

        String photoName = StringUtils.cleanPath(photo.getOriginalFilename());

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

            Photo photoDto = new Photo(photoName, photo.getContentType(), photo.getBytes());
            Photo photoDb = photoRepository.save(photoDto);
            String downloadURl = prepareDownloadUrl(photoDb.getId());

            return new PhotoResponse(
                    photoDb.getId(),
                    photo.getName(),
                    downloadURl,
                    photo.getContentType(),
                    photo.getSize()
            );

        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(photo.getSize());
        } catch (Exception e) {
            throw new Exception("Could not upload the file: " + photoName + "!"); // HttpStatus.EXPECTATION_FAILED
        }
    }

    @Override
    public List<PhotoResponse> multipleUpload(MultipleUploadRequest multipleUploadRequest) {
        List<PhotoResponse> photos = new ArrayList<>();

        Arrays
                .stream(multipleUploadRequest.getPhotos())
                .forEach(photo -> {
                    try {
                        SingleUploadRequest singleUploadRequest = new SingleUploadRequest(photo);
                        photos.add(singleUpload(singleUploadRequest));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return photos;
    }

    @Override
    public List<PhotoResponse> findAll() {
        List<Photo> photos = photoRepository.findAll();

        return photos
                .stream()
                .map(photo -> new PhotoResponse(
                        photo.getId(),
                        photo.getName(),
                        prepareDownloadUrl(photo.getId()),
                        photo.getType(),
                        photo.getSourceData().length)
                )
                .toList();
    }

    @Override
    public PhotoResponse findById(String photoId) {
        Optional<Photo> photo = photoRepository.findById(photoId);

        if (photo.isEmpty()) {
            throw new RuntimeException("Could not find the photo with id : " + photoId);
        }

        Photo photoDto = photo.get();

        return new PhotoResponse(
                photoDto.getId(),
                photoDto.getName(),
                prepareDownloadUrl(photoDto.getId()),
                photoDto.getType(),
                photoDto.getSourceData().length
        );
    }

    @Override
    public DownloadResponse downloadById(String photoId) {
        try {
            Optional<Photo> photo = photoRepository.findById(photoId);

            if (photo.isEmpty()) {
                throw new RuntimeException("Could not find the photo with id : " + photoId);
            }

            byte[] data = photo.get().getSourceData();
            ByteArrayResource resource = new ByteArrayResource(data);

            if (resource.exists() || resource.isReadable()) {
                return new DownloadResponse(
                        resource,
                        photo.get().getName()
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
    public Boolean deleteById(String photoId) {
        Optional<Photo> photo = photoRepository.findById(photoId);

        if (photo.isEmpty()) {
            throw new RuntimeException("Could not find the photo with id : " + photoId);
        }

        photoRepository.deleteById(photo.get().getId());

        return !photoRepository.existsById(photoId);
    }

    private String prepareDownloadUrl(String photoId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/v1/photos/download/")
                .path(photoId)
                .toUriString();
    }

}
