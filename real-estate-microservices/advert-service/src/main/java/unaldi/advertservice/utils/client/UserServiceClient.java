package unaldi.advertservice.utils.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import unaldi.advertservice.utils.client.dto.response.RestResponse;
import unaldi.advertservice.utils.client.dto.response.UserResponse;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@FeignClient(name = "user-service", url = "http://${USER_SERVICE_HOST:localhost}:8081")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{userId}")
    ResponseEntity<RestResponse<UserResponse>> findById(@PathVariable("userId") Long userId);

}
