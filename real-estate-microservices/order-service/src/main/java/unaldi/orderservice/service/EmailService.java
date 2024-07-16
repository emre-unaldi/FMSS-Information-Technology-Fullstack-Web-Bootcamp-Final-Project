package unaldi.orderservice.service;

import unaldi.orderservice.entity.dto.EmailDetailsDTO;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
public interface EmailService {

    void sendEmail(EmailDetailsDTO emailDetailsDTO);

}
