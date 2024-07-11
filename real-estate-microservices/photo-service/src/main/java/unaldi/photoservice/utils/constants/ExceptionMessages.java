package unaldi.photoservice.utils.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessages {

    public static final String PHOTO_NAME_EMPTY = "Photo name cannot be null or empty";
    public static final String INVALID_PHOTO_FORMAT = "Invalid file format. Only image files are allowed";
    public static final String INVALID_PHOTO_NAME_PATH_SEQUENCE = "Photo name contains invalid path sequence";
    public static final String PHOTO_NAME_ALREADY_EXISTS = "Photo name already exists : ";
    public static final String PHOTO_SIZE_MAX_LIMIT= "Photo size exceeds maximum limit";
    public static final String PHOTO_NOT_FOUND = "Photo not found in database";
    public static final String BAD_REQUEST = "Your request is not valid. Please try to correct the format";
    public static final String INTERNAL_SERVER_ERROR = "An error occurred on the server side. Please try again";
    public static final String PHOTO_NOT_READ = "Photo source could not be read";

}
