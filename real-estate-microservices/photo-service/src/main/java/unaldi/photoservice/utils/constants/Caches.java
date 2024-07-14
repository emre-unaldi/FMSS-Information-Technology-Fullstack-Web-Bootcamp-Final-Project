package unaldi.photoservice.utils.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 14.07.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Caches {

    public static final String PHOTO_CACHE = "photo";
    public static final String PHOTOS_CACHE = "photos";
    public static final String PHOTOS_BY_IDS_CACHE = "photos-by-ids";
    public static final String KEY_PREFIX = "photo-service:";

}
