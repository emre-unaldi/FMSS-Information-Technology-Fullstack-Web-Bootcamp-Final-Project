package unaldi.advertservice.service.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import unaldi.advertservice.entity.Address;
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

    AdvertMapper INSTANCE = Mappers.getMapper( AdvertMapper.class );

    @Mapping(target = "id", ignore = true)
    Address addressSaveRequestToAddress(AdvertSaveRequest advertSaveRequest);

    Address addressUpdateRequestToAddress(AdvertUpdateRequest advertUpdateRequest);

    AddressResponse addressToAddressResponse(Address address);

    @IterableMapping(elementTargetType = AddressResponse.class)
    List<AddressResponse> addressesToAddressResponses(List<Address> addresses);

}
