package unaldi.advertservice.service.advert;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import unaldi.advertservice.entity.Address;
import unaldi.advertservice.entity.Advert;
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertStatusUpdateRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;
import unaldi.advertservice.entity.enums.AdvertStatus;
import unaldi.advertservice.entity.enums.AdvertType;
import unaldi.advertservice.entity.enums.HousingType;
import unaldi.advertservice.utils.client.dto.response.PhotoResponse;
import unaldi.advertservice.utils.client.dto.response.UserResponse;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 22.07.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectFactory {
    private static ObjectFactory instance;
    private AdvertSaveRequest advertSaveRequest;
    private UserResponse userResponse;
    private List<PhotoResponse> photoResponses;
    private Address address;
    private Advert advert;
    private AddressResponse addressResponse;
    private AdvertUpdateRequest advertUpdateRequest;
    private AdvertStatusUpdateRequest advertStatusUpdateRequest;

    public static synchronized ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    public AdvertSaveRequest getAdvertSaveRequest() {
        if (advertSaveRequest == null) {
            advertSaveRequest = new AdvertSaveRequest();
            advertSaveRequest.setPhotoIds(List.of("photo1", "photo2"));
            advertSaveRequest.setUserId(1L);
            advertSaveRequest.setHousingType(HousingType.APARTMENT);
            advertSaveRequest.setAdvertType(AdvertType.FOR_SENT);
            advertSaveRequest.setTitle("Beautiful Apartment");
            advertSaveRequest.setDescription("A beautiful apartment in the city center.");
            advertSaveRequest.setAddressId(1L);
            advertSaveRequest.setReleaseDate(LocalDate.now());
            advertSaveRequest.setValidityDate(LocalDate.now().plusMonths(6));
            advertSaveRequest.setArea(120);
            advertSaveRequest.setNumberOfRooms(3);
            advertSaveRequest.setPrice(150000L);
            advertSaveRequest.setIsBalcony(true);
            advertSaveRequest.setIsCarPark(true);
        }
        return advertSaveRequest;
    }

    public UserResponse getUserResponse() {
        if (userResponse == null) {
            userResponse = new UserResponse();
            userResponse.setId(1L);
            userResponse.setFirstName("John");
            userResponse.setLastName("Doe");
            userResponse.setUsername("johnDoe");
            userResponse.setEmail("john.doe@example.com");
            userResponse.setPassword("password");
            userResponse.setPhoneNumber("1234567890");
            userResponse.setRoles(List.of("USER"));
        }
        return userResponse;
    }

    public Address getAddress() {
        if (address == null) {
            address = new Address();
            address.setId(1L);
            address.setNeighborhood("Downtown");
            address.setStreet("Main Street");
            address.setProvince("Some Province");
            address.setCounty("Some County");
            address.setZipCode("12345");
        }
        return address;
    }

    public Advert getAdvert() {
        if (advert == null) {
            advert = new Advert();
            advert.setId(1L);
            advert.setAdvertNumber("AD123456");
            advert.setPhotoIds(List.of("photo1", "photo2"));
            advert.setUserId(1L);
            advert.setHousingType(HousingType.APARTMENT);
            advert.setAdvertStatus(AdvertStatus.ACTIVE);
            advert.setAdvertType(AdvertType.FOR_SENT);
            advert.setTitle("Beautiful Apartment");
            advert.setDescription("A beautiful apartment in the city center.");
            advert.setAddress(getAddress());
            advert.setReleaseDate(LocalDate.now());
            advert.setValidityDate(LocalDate.now().plusMonths(6));
            advert.setArea(120);
            advert.setNumberOfRooms(3);
            advert.setPrice(150000L);
            advert.setIsBalcony(true);
            advert.setIsCarPark(true);
        }
        return advert;
    }

    public AdvertStatusUpdateRequest getAdvertStatusUpdateRequest() {
        if (advertStatusUpdateRequest == null) {
            advertStatusUpdateRequest = new AdvertStatusUpdateRequest();
            advertStatusUpdateRequest.setId(1L);
            advertStatusUpdateRequest.setAdvertStatus(AdvertStatus.ACTIVE);
        }
        return advertStatusUpdateRequest;
    }

    public AddressResponse getAddressResponse() {
        if (addressResponse == null) {
            addressResponse = new AddressResponse();
            addressResponse.setId(1L);
            addressResponse.setNeighborhood("Downtown");
            addressResponse.setStreet("Main Street");
            addressResponse.setProvince("Some Province");
            addressResponse.setCounty("Some County");
            addressResponse.setZipCode("12345");
        }
        return addressResponse;
    }

    public List<PhotoResponse> getPhotoResponseList() {
        if (photoResponses == null) {
            photoResponses = Arrays.asList(
                    new PhotoResponse() {{
                        setId("photo1");
                        setName("Photo 1");
                        setDownloadUrl("https://example.com/photo1");
                        setType("image/jpeg");
                        setSize(2048L);
                    }},
                    new PhotoResponse() {{
                        setId("photo2");
                        setName("Photo 2");
                        setDownloadUrl("https://example.com/photo2");
                        setType("image/jpeg");
                        setSize(4096L);
                    }}
            );
        }
        return photoResponses;
    }

    public AdvertUpdateRequest getAdvertUpdateRequest() {
        if (advertUpdateRequest == null) {
            advertUpdateRequest = new AdvertUpdateRequest();
            advertUpdateRequest.setId(1L);
            advertUpdateRequest.setPhotoIds(List.of("photo1", "photo2"));
            advertUpdateRequest.setUserId(1L);
            advertUpdateRequest.setHousingType(HousingType.APARTMENT);
            advertUpdateRequest.setAdvertStatus(AdvertStatus.ACTIVE);
            advertUpdateRequest.setAdvertType(AdvertType.FOR_SENT);
            advertUpdateRequest.setTitle("Updated Apartment");
            advertUpdateRequest.setDescription("An updated description of the apartment.");
            advertUpdateRequest.setAddressId(1L);
            advertUpdateRequest.setReleaseDate(LocalDate.now());
            advertUpdateRequest.setValidityDate(LocalDate.now().plusMonths(6));
            advertUpdateRequest.setArea(120);
            advertUpdateRequest.setNumberOfRooms(3);
            advertUpdateRequest.setPrice(155000L);
            advertUpdateRequest.setIsBalcony(true);
            advertUpdateRequest.setIsCarPark(true);
        }
        return advertUpdateRequest;
    }

    public List<Advert> getAdvertList() {
        if (advert == null) {
            return Arrays.asList(
                    new Advert() {{
                        setId(1L);
                        setAdvertNumber("AD123456");
                        setPhotoIds(List.of("photo1", "photo2"));
                        setUserId(1L);
                        setHousingType(HousingType.APARTMENT);
                        setAdvertStatus(AdvertStatus.ACTIVE);
                        setAdvertType(AdvertType.FOR_SENT);
                        setTitle("Beautiful Apartment");
                        setDescription("A beautiful apartment in the city center.");
                        setAddress(getAddress());
                        setReleaseDate(LocalDate.now());
                        setValidityDate(LocalDate.now().plusMonths(6));
                        setArea(120);
                        setNumberOfRooms(3);
                        setPrice(150000L);
                        setIsBalcony(true);
                        setIsCarPark(true);
                    }},
                    new Advert() {{
                        setId(2L);
                        setAdvertNumber("AD789012");
                        setPhotoIds(List.of("photo3", "photo4"));
                        setUserId(2L);
                        setHousingType(HousingType.DETACHED_HOUSE);
                        setAdvertStatus(AdvertStatus.ACTIVE);
                        setAdvertType(AdvertType.FOR_SENT);
                        setTitle("Cozy House");
                        setDescription("A cozy house in a quiet neighborhood.");
                        setAddress(new Address() {{
                            setId(2L);
                            setNeighborhood("Suburbia");
                            setStreet("Elm Street");
                            setProvince("Another Province");
                            setCounty("Another County");
                            setZipCode("67890");
                        }});
                        setReleaseDate(LocalDate.now().minusMonths(2));
                        setValidityDate(LocalDate.now().plusMonths(4));
                        setArea(150);
                        setNumberOfRooms(4);
                        setPrice(200000L);
                        setIsBalcony(false);
                        setIsCarPark(true);
                    }}
            );
        }
        return Collections.singletonList(advert);
    }
}
