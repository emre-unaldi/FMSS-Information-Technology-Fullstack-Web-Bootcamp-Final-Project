package unaldi.advertservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unaldi.advertservice.entity.Advert;
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AdvertResponse;
import unaldi.advertservice.repository.AdvertRepository;
import unaldi.advertservice.service.AdvertService;
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
@Service
public class AdvertServiceImpl implements AdvertService {

    private final AdvertRepository advertRepository;

    @Autowired
    public AdvertServiceImpl(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    @Override
    public DataResult<AdvertResponse> save(AdvertSaveRequest advertSaveRequest) {

        return null;
    }

    @Override
    public DataResult<AdvertResponse> update(AdvertUpdateRequest advertUpdateRequest) {
        return null;
    }

    @Override
    public DataResult<AdvertResponse> findById(Long advertId) {
        return null;
    }

    @Override
    public DataResult<List<AdvertResponse>> findAll() {
        return null;
    }

    @Override
    public Result deleteById(Long advertId) {
        return null;
    }
}
