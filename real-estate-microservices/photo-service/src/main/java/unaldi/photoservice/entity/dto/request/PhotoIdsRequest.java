package unaldi.photoservice.entity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoIdsRequest {
    private List<String> photoIds;
}
