package unaldi.photoservice.entity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleUploadRequest {
    private MultipartFile photo;
}
