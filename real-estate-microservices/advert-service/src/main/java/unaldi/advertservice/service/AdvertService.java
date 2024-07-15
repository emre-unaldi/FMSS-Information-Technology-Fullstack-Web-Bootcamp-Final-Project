package unaldi.advertservice.service;

import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertStatusUpdateRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AdvertResponse;
import unaldi.advertservice.entity.enums.AdvertStatus;
import unaldi.advertservice.utils.result.DataResult;
import unaldi.advertservice.utils.result.Result;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
public interface AdvertService {

    DataResult<AdvertResponse> save(AdvertSaveRequest advertSaveRequest);
    DataResult<AdvertResponse> update(AdvertUpdateRequest advertUpdateRequest);
    DataResult<AdvertResponse> findById(Long advertId);
    DataResult<List<AdvertResponse>> findAll();
    Result deleteById(Long advertId);
    DataResult<AdvertResponse> changeStatus(AdvertStatusUpdateRequest advertStatusUpdateRequest);

}
