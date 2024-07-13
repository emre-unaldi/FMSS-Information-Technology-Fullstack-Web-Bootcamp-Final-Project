package unaldi.advertservice.service.Impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unaldi.advertservice.entity.Address;
import unaldi.advertservice.entity.Advert;
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;
import unaldi.advertservice.entity.dto.response.AdvertResponse;
import unaldi.advertservice.repository.AdvertRepository;
import unaldi.advertservice.service.AddressService;
import unaldi.advertservice.service.mapper.AddressMapper;
import unaldi.advertservice.service.mapper.AdvertMapper;
import unaldi.advertservice.service.AdvertService;
import unaldi.advertservice.utils.client.PhotoServiceClient;
import unaldi.advertservice.utils.client.UserServiceClient;
import unaldi.advertservice.utils.client.dto.request.PhotoIdsRequest;
import unaldi.advertservice.utils.client.dto.response.PhotoResponse;
import unaldi.advertservice.utils.client.dto.response.RestResponse;
import unaldi.advertservice.utils.client.dto.response.UserResponse;
import unaldi.advertservice.utils.constants.ExceptionMessages;
import unaldi.advertservice.utils.constants.Messages;
import unaldi.advertservice.utils.exception.AdvertNotFoundException;
import unaldi.advertservice.utils.result.DataResult;
import unaldi.advertservice.utils.result.Result;
import unaldi.advertservice.utils.result.SuccessDataResult;
import unaldi.advertservice.utils.result.SuccessResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final UserServiceClient userServiceClient;
    private final PhotoServiceClient photoServiceClient;
    private final AddressService addressService;
    private final EntityManager entityManager;

    @Autowired
    public AdvertServiceImpl(AdvertRepository advertRepository, UserServiceClient userServiceClient, PhotoServiceClient photoServiceClient, AddressService addressService, EntityManager entityManager) {
        this.advertRepository = advertRepository;
        this.userServiceClient = userServiceClient;
        this.photoServiceClient = photoServiceClient;
        this.addressService = addressService;
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public DataResult<AdvertResponse> save(AdvertSaveRequest advertSaveRequest) {
        Address managedAddress = entityManager.merge(fetchAddress(advertSaveRequest.getAddressId()));

        Advert advert = AdvertMapper.INSTANCE.advertSaveRequestToAdvert(advertSaveRequest);
        advert.setAdvertNumber(UUID.randomUUID().toString());
        advert.setAddress(managedAddress);

        AdvertResponse advertResponse = AdvertMapper.INSTANCE.advertToAdvertResponse(advert);
        advertResponse.setUser(fetchUser(advert.getUserId()));
        advertResponse.setPhotos(fetchPhotos(advert.getPhotoIds()));

        advertRepository.save(advert);
        advertResponse.setId(advert.getId());

        return new SuccessDataResult<>(advertResponse, Messages.ADVERT_SAVED);
    }

    @Override
    public DataResult<AdvertResponse> update(AdvertUpdateRequest advertUpdateRequest) {
        Advert foundAdvert = advertRepository
                .findById(advertUpdateRequest.getId())
                .orElseThrow(() -> new AdvertNotFoundException(ExceptionMessages.ADVERT_NOT_FOUND));

        Advert advert = AdvertMapper.INSTANCE.advertUpdateRequestToAdvert(advertUpdateRequest);
        advert.setAdvertNumber(foundAdvert.getAdvertNumber());

        AdvertResponse advertResponse = AdvertMapper.INSTANCE.advertToAdvertResponse(advert);
        advertResponse.setUser(fetchUser(advert.getUserId()));
        advertResponse.setPhotos(fetchPhotos(advert.getPhotoIds()));

        advertRepository.save(advert);
        advertResponse.setId(advert.getId());

        return new SuccessDataResult<>(advertResponse, Messages.ADVERT_UPDATED);
    }

    @Override
    public DataResult<AdvertResponse> findById(Long advertId) {
        Optional<Advert> foundAdvert = advertRepository.findById(advertId);

        if (foundAdvert.isEmpty()) {
            throw new AdvertNotFoundException(ExceptionMessages.ADVERT_NOT_FOUND);
        }

        AdvertResponse advertResponse = AdvertMapper.INSTANCE.advertToAdvertResponse(foundAdvert.get());
        advertResponse.setUser(fetchUser(foundAdvert.get().getUserId()));
        advertResponse.setPhotos(fetchPhotos(foundAdvert.get().getPhotoIds()));

        return new SuccessDataResult<>(advertResponse, Messages.ADVERT_FOUND);
    }

    @Override
    public DataResult<List<AdvertResponse>> findAll() {
        List<Advert> adverts = advertRepository.findAll();

        List<AdvertResponse> advertResponses = adverts.stream()
                .map(advert -> {
                    AdvertResponse advertResponse = AdvertMapper.INSTANCE.advertToAdvertResponse(advert);
                    advertResponse.setUser(fetchUser(advert.getUserId()));
                    advertResponse.setPhotos(fetchPhotos(advert.getPhotoIds()));

                    return advertResponse;
                })
                .toList();

        return new SuccessDataResult<>(advertResponses, Messages.ADVERTS_LISTED);
    }

    @Override
    public Result deleteById(Long advertId) {
        Advert advert = advertRepository
                .findById(advertId)
                .orElseThrow(() -> new AdvertNotFoundException(ExceptionMessages.ADVERT_NOT_FOUND));

        advertRepository.deleteById(advert.getId());

        return new SuccessResult(Messages.ADVERT_DELETED);
    }

    private UserResponse fetchUser(Long userId) {
        ResponseEntity<RestResponse<UserResponse>> userResponse = userServiceClient.findById(userId);
        return Objects.requireNonNull(userResponse.getBody()).getData();
    }

    private List<PhotoResponse> fetchPhotos(List<String> photoIds) {
        PhotoIdsRequest photoIdsRequest = PhotoIdsRequest.builder().photoIds(photoIds).build();
        ResponseEntity<RestResponse<List<PhotoResponse>>> photoResponse = photoServiceClient.findByPhotoIds(photoIdsRequest);
        return Objects.requireNonNull(photoResponse.getBody()).getData();
    }

    private Address fetchAddress(Long addressId) {
        AddressResponse addressResponse = addressService.findById(addressId).getData();
        return AddressMapper.INSTANCE.addressResponseToAddress(addressResponse);
    }

}
