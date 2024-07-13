package unaldi.advertservice.service;

import unaldi.advertservice.entity.dto.request.AddressSaveRequest;
import unaldi.advertservice.entity.dto.request.AddressUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;
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
public interface AddressService {

    DataResult<AddressResponse> save(AddressSaveRequest addressSaveRequest);
    DataResult<AddressResponse> update(AddressUpdateRequest addressUpdateRequest);
    DataResult<AddressResponse> findById(Long addressId);
    DataResult<List<AddressResponse>> findAll();
    Result deleteById(Long addressId);

}
