package unaldi.photoservice.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import unaldi.photoservice.entity.Photo;
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
@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoMapper INSTANCE = Mappers.getMapper( PhotoMapper.class );

    @Mapping(target = "id", ignore = true)
    Photo uploadRequestToPhoto(String name, String type, byte[] sourceData);

    @Mappings({
            @Mapping(target = "size", expression = "java(photo.getSourceData().length)"),
            @Mapping(target = "downloadUrl", ignore = true)
    })
    PhotoResponse photoToPhotoResponse(Photo photo);

    @IterableMapping(elementTargetType = PhotoResponse.class)
    List<PhotoResponse> photosToPhotoResponses(List<Photo> photos);

    @AfterMapping
    default void setDownloadUrl(Photo photo, @MappingTarget PhotoResponse photoResponse) {
        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/v1/photos/download/")
                .path(photo.getId())
                .toUriString();

        photoResponse.setDownloadUrl(downloadUrl);
    }

    @Mapping(target = "name", source = "photo.name")
    DownloadResponse photoToDownloadResponse(Photo photo, ByteArrayResource resource);

    SingleUploadRequest multipartFileToSingleUploadRequest(MultipartFile photo);
}
