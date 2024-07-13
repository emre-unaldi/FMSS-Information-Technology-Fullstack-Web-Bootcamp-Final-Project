package unaldi.advertservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unaldi.advertservice.entity.Advert;
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AdvertResponse;
import unaldi.advertservice.repository.AdvertRepository;
import unaldi.advertservice.service.mapper.AdvertMapper;
import unaldi.advertservice.service.AdvertService;
import unaldi.advertservice.utils.constants.ExceptionMessages;
import unaldi.advertservice.utils.constants.Messages;
import unaldi.advertservice.utils.exception.AdvertNotFoundException;
import unaldi.advertservice.utils.result.DataResult;
import unaldi.advertservice.utils.result.Result;
import unaldi.advertservice.utils.result.SuccessDataResult;
import unaldi.advertservice.utils.result.SuccessResult;

import java.util.List;
import java.util.UUID;

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
        Advert advert = AdvertMapper.INSTANCE.advertSaveRequestToAdvert(advertSaveRequest);
        advert.setAdvertNumber(UUID.randomUUID().toString());
        advertRepository.save(advert);

        return new SuccessDataResult<>(
                AdvertMapper.INSTANCE.advertToAdvertResponse(advert),
                Messages.ADVERT_SAVED
        );
    }

    @Override
    public DataResult<AdvertResponse> update(AdvertUpdateRequest advertUpdateRequest) {
        Advert foundAdvert = advertRepository
                .findById(advertUpdateRequest.getId())
                .orElseThrow(() -> new AdvertNotFoundException(ExceptionMessages.ADVERT_NOT_FOUND));

        Advert advert = AdvertMapper.INSTANCE.advertUpdateRequestToAdvert(advertUpdateRequest);
        advert.setAdvertNumber(foundAdvert.getAdvertNumber());
        advertRepository.save(advert);

        return new SuccessDataResult<>(
                AdvertMapper.INSTANCE.advertToAdvertResponse(advert),
                Messages.ADVERT_UPDATED
        );
    }

    @Override
    public DataResult<AdvertResponse> findById(Long advertId) {
        AdvertResponse advertResponse = advertRepository
                .findById(advertId)
                .map(AdvertMapper.INSTANCE::advertToAdvertResponse)
                .orElseThrow(() -> new AdvertNotFoundException(ExceptionMessages.ADVERT_NOT_FOUND));

        return new SuccessDataResult<>(advertResponse, Messages.ADVERT_FOUND);
    }

    @Override
    public DataResult<List<AdvertResponse>> findAll() {
        List<Advert> adverts = advertRepository.findAll();

        return new SuccessDataResult<>(
                AdvertMapper.INSTANCE.advertsToAdvertResponses(adverts),
                Messages.ADVERTS_LISTED
        );
    }

    @Override
    public Result deleteById(Long advertId) {
        Advert advert = advertRepository
                .findById(advertId)
                .orElseThrow(() -> new AdvertNotFoundException(ExceptionMessages.ADVERT_NOT_FOUND));

        advertRepository.deleteById(advert.getId());

        return new SuccessResult(Messages.ADVERT_DELETED);
    }

}
