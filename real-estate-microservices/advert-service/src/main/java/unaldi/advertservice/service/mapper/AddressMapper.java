package unaldi.advertservice.service.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import unaldi.advertservice.entity.Address;
import unaldi.advertservice.entity.dto.request.AddressSaveRequest;
import unaldi.advertservice.entity.dto.request.AddressUpdateRequest;
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper( AddressMapper.class );

    @Mapping(target = "id", ignore = true)
    Address addressSaveRequestToAddress(AddressSaveRequest addressSaveRequest);

    Address addressUpdateRequestToAddress(AddressUpdateRequest addressUpdateRequest);

    AddressResponse addressToAddressResponse(Address address);

    @IterableMapping(elementTargetType = AddressResponse.class)
    List<AddressResponse> addressesToAddressResponses(List<Address> addresses);

}
