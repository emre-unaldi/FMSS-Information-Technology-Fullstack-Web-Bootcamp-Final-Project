package unaldi.advertservice.service.Impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import unaldi.advertservice.utils.constants.Caches;
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
    private final CacheManager cacheManager;

    @Autowired
    public AdvertServiceImpl(AdvertRepository advertRepository, UserServiceClient userServiceClient, PhotoServiceClient photoServiceClient, AddressService addressService, EntityManager entityManager, CacheManager cacheManager) {
        this.advertRepository = advertRepository;
        this.userServiceClient = userServiceClient;
        this.photoServiceClient = photoServiceClient;
        this.addressService = addressService;
        this.entityManager = entityManager;
        this.cacheManager = cacheManager;
    }

    @CacheEvict(value = Caches.ADVERTS_CACHE, allEntries = true, condition = "#result.success != false")
    @Transactional
    @Override
    public DataResult<AdvertResponse> save(AdvertSaveRequest advertSaveRequest) {
        UserResponse userResponse = fetchUser(advertSaveRequest.getUserId());
        List<PhotoResponse> photoResponses = fetchPhotos(advertSaveRequest.getPhotoIds());
        Address address = fetchAddress(advertSaveRequest.getAddressId());

        Advert advert = AdvertMapper.INSTANCE.advertSaveRequestToAdvert(advertSaveRequest);
        advert.setAddress(address);
        advertRepository.save(advert);

        AdvertResponse advertResponse = AdvertMapper.INSTANCE.advertToAdvertResponse(advert);
        advertResponse.setUser(userResponse);
        advertResponse.setPhotos(photoResponses);

        return new SuccessDataResult<>(advertResponse, Messages.ADVERT_SAVED);
    }

    @Caching(
            put = { @CachePut(value = Caches.ADVERT_CACHE, key = "#advertUpdateRequest.getId()", unless = "#result.success != true") },
            evict = { @CacheEvict(value = Caches.ADVERTS_CACHE, allEntries = true, condition = "#result.success != false") }
    )
    @Transactional
    @Override
    public DataResult<AdvertResponse> update(AdvertUpdateRequest advertUpdateRequest) {
        Advert foundAdvert = advertRepository
                .findById(advertUpdateRequest.getId())
                .orElseThrow(() -> new AdvertNotFoundException(ExceptionMessages.ADVERT_NOT_FOUND));

        UserResponse userResponse = fetchUser(advertUpdateRequest.getUserId());
        List<PhotoResponse> photoResponses = fetchPhotos(advertUpdateRequest.getPhotoIds());
        Address address = fetchAddress(advertUpdateRequest.getAddressId());

        Advert advert = AdvertMapper.INSTANCE.advertUpdateRequestToAdvert(advertUpdateRequest, foundAdvert.getAdvertNumber());
        advert.setAddress(address);
        advertRepository.save(advert);

        AdvertResponse advertResponse = AdvertMapper.INSTANCE.advertToAdvertResponse(advert);
        advertResponse.setUser(userResponse);
        advertResponse.setPhotos(photoResponses);

        return new SuccessDataResult<>(advertResponse, Messages.ADVERT_UPDATED);
    }

    @Cacheable(value = Caches.ADVERT_CACHE, key = "#advertId", unless = "#result.success != true")
    @Override
    public DataResult<AdvertResponse> findById(Long advertId) {
        Optional<Advert> foundAdvert = advertRepository.findById(advertId);

        if (foundAdvert.isEmpty()) {
            throw new AdvertNotFoundException(ExceptionMessages.ADVERT_NOT_FOUND);
        }

        UserResponse userResponse = fetchUser(foundAdvert.get().getUserId());
        List<PhotoResponse> photoResponses = fetchPhotos(foundAdvert.get().getPhotoIds());

        AdvertResponse advertResponse = AdvertMapper.INSTANCE.advertToAdvertResponse(foundAdvert.get());
        advertResponse.setUser(userResponse);
        advertResponse.setPhotos(photoResponses);

        return new SuccessDataResult<>(advertResponse, Messages.ADVERT_FOUND);
    }

    @Cacheable(value = Caches.ADVERTS_CACHE, key = "'all'", unless = "#result.success != true")
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

    @Caching(
            evict = {
                    @CacheEvict(value = Caches.ADVERTS_CACHE, allEntries = true, condition = "#result.success != false"),
                    @CacheEvict(value = Caches.ADVERT_CACHE, key = "#advertId", condition = "#result.success != false")
            }
    )
    @Override
    public Result deleteById(Long advertId) {
        Advert advert = advertRepository
                .findById(advertId)
                .orElseThrow(() -> new AdvertNotFoundException(ExceptionMessages.ADVERT_NOT_FOUND));

        Cache foundAddressCache = cacheManager.getCache(Caches.ADDRESS_CACHE);
        if (foundAddressCache != null) {
            foundAddressCache.evict(advert.getAddress().getId());
        }

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
        Address address = AddressMapper.INSTANCE.addressResponseToAddress(addressResponse);

        return entityManager.merge(address);
    }

}
