package unaldi.advertservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unaldi.advertservice.entity.Address;
import unaldi.advertservice.entity.dto.request.AddressSaveRequest;
import unaldi.advertservice.entity.dto.request.AddressUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;
import unaldi.advertservice.repository.AddressRepository;
import unaldi.advertservice.service.AddressService;
import unaldi.advertservice.service.mapper.AddressMapper;
import unaldi.advertservice.utils.constants.ExceptionMessages;
import unaldi.advertservice.utils.constants.Messages;
import unaldi.advertservice.utils.exception.AddressNotFoundException;
import unaldi.advertservice.utils.result.DataResult;
import unaldi.advertservice.utils.result.Result;
import unaldi.advertservice.utils.result.SuccessDataResult;
import unaldi.advertservice.utils.result.SuccessResult;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public DataResult<AddressResponse> save(AddressSaveRequest addressSaveRequest) {
        Address address = AddressMapper.INSTANCE.addressSaveRequestToAddress(addressSaveRequest);
        addressRepository.save(address);

        return new SuccessDataResult<>(
                AddressMapper.INSTANCE.addressToAddressResponse(address),
                Messages.ADDRESS_SAVED
        );
    }

    @Override
    public DataResult<AddressResponse> update(AddressUpdateRequest addressUpdateRequest) {
        if (!addressRepository.existsById(addressUpdateRequest.getId())) {
            throw new AddressNotFoundException(ExceptionMessages.ADDRESS_NOT_FOUND);
        }

        Address address = AddressMapper.INSTANCE.addressUpdateRequestToAddress(addressUpdateRequest);
        addressRepository.save(address);

        return new SuccessDataResult<>(
                AddressMapper.INSTANCE.addressToAddressResponse(address),
                Messages.ADDRESS_UPDATED
        );
    }

    @Override
    public DataResult<AddressResponse> findById(Long addressId) {
        AddressResponse addressResponse = addressRepository
                .findById(addressId)
                .map(AddressMapper.INSTANCE::addressToAddressResponse)
                .orElseThrow(() -> new AddressNotFoundException(ExceptionMessages.ADDRESS_NOT_FOUND));

        return new SuccessDataResult<>(addressResponse, Messages.ADDRESS_FOUND);
    }

    @Override
    public DataResult<List<AddressResponse>> findAll() {
        List<Address> addresses = addressRepository.findAll();

        return new SuccessDataResult<>(
                AddressMapper.INSTANCE.addressesToAddressResponses(addresses),
                Messages.ADDRESSES_LISTED
        );
    }

    @Override
    public Result deleteById(Long addressId) {
        Address address = addressRepository
                .findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(ExceptionMessages.ADDRESS_NOT_FOUND));

        addressRepository.deleteById(address.getId());

        return new SuccessResult(Messages.ADDRESS_DELETED);
    }
}