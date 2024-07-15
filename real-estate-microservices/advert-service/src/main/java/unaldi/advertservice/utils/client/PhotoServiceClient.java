package unaldi.advertservice.utils.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import unaldi.advertservice.utils.client.dto.request.PhotoIdsRequest;
import unaldi.advertservice.utils.client.dto.response.PhotoResponse;
import unaldi.advertservice.utils.client.dto.response.RestResponse;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@FeignClient(name = "photo-service", url = "http://${PHOTO_SERVICE_HOST:localhost}:8080")
public interface PhotoServiceClient {

    @PostMapping("/api/v1/photos/findByIds")
    ResponseEntity<RestResponse<List<PhotoResponse>>> findByPhotoIds(@RequestBody PhotoIdsRequest photoIdsRequest);

}
