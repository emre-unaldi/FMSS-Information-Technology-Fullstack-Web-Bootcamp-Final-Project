package unaldi.advertservice.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import unaldi.advertservice.entity.Advert;
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AdvertResponse;
import unaldi.advertservice.entity.enums.AdvertStatus;

import java.util.List;
import java.util.UUID;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Mapper(componentModel = "spring")
public interface AdvertMapper {

    AdvertMapper INSTANCE = Mappers.getMapper( AdvertMapper.class );

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "advertNumber", expression = "java(setAdvertNumber())"),
            @Mapping(target = "advertStatus", expression = "java(setAdvertStatus())"),
    })
    Advert advertSaveRequestToAdvert(AdvertSaveRequest advertSaveRequest);

    @AfterMapping
    default String setAdvertNumber() {
        return UUID.randomUUID().toString();
    }

    @AfterMapping
    default AdvertStatus setAdvertStatus() {
        return AdvertStatus.IN_REVIEW;
    }

    @Mappings({
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "advertNumber", source = "foundAdvertNumber")
    })
    Advert advertUpdateRequestToAdvert(AdvertUpdateRequest advertUpdateRequest, String foundAdvertNumber);

    @Mappings({
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "photos", ignore = true)
    })
    AdvertResponse advertToAdvertResponse(Advert advert);

    @IterableMapping(elementTargetType = AdvertResponse.class)
    List<AdvertResponse> advertsToAdvertResponses(List<Advert> adverts);

}
