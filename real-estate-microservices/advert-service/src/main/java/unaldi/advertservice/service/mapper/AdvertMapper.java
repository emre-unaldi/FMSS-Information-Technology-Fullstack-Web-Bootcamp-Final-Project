package unaldi.advertservice.service.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import unaldi.advertservice.entity.Advert;
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AdvertResponse;

import java.util.List;

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
            @Mapping(target = "advertNumber", ignore = true)
    })
    Advert advertSaveRequestToAdvert(AdvertSaveRequest advertSaveRequest);

    @Mapping(target = "advertNumber", ignore = true)
    Advert advertUpdateRequestToAdvert(AdvertUpdateRequest advertUpdateRequest);

    AdvertResponse advertToAdvertResponse(Advert advert);

    @IterableMapping(elementTargetType = AdvertResponse.class)
    List<AdvertResponse> advertsToAdvertResponses(List<Advert> adverts);

}
