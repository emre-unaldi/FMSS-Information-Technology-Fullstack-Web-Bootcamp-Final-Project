package unaldi.photoservice.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class PhotoResponse {
    private String id;
    private String name;
    private String downloadUrl;
    private String type;
    private long size;
}
