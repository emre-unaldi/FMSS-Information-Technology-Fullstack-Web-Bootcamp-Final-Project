package unaldi.photoservice.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

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
public class DownloadResponse {
    private Resource resource;
    private String name;
}
