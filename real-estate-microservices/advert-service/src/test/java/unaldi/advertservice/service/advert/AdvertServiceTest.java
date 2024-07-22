package unaldi.advertservice.service.advert;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import unaldi.advertservice.entity.Advert;
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertStatusUpdateRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;
import unaldi.advertservice.entity.dto.response.AdvertResponse;
import unaldi.advertservice.entity.enums.AdvertStatus;
import unaldi.advertservice.repository.AdvertRepository;
import unaldi.advertservice.service.AddressService;
import unaldi.advertservice.service.Impl.AdvertServiceImpl;
import unaldi.advertservice.utils.client.PhotoServiceClient;
import unaldi.advertservice.utils.client.UserServiceClient;
import unaldi.advertservice.utils.client.dto.request.PhotoIdsRequest;
import unaldi.advertservice.utils.client.dto.response.PhotoResponse;
import unaldi.advertservice.utils.client.dto.response.RestResponse;
import unaldi.advertservice.utils.client.dto.response.UserResponse;
import unaldi.advertservice.utils.constants.ExceptionMessages;
import unaldi.advertservice.utils.exception.AdvertNotFoundException;
import unaldi.advertservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.advertservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.advertservice.utils.result.DataResult;
import unaldi.advertservice.utils.result.Result;
import unaldi.advertservice.utils.result.SuccessDataResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdvertServiceTest {
    private static Advert advert;
    private static List<Advert> advertList;
    private static AdvertSaveRequest advertSaveRequest;
    private static AdvertUpdateRequest advertUpdateRequest;
    private static AdvertStatusUpdateRequest advertStatusUpdateRequest;
    private static UserResponse userResponse;
    private static List<PhotoResponse> photoResponses;
    private static AddressResponse addressResponse;

    @Mock
    private AdvertRepository advertRepository;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private PhotoServiceClient photoServiceClient;
    @Mock
    private AddressService addressService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private LogProducer logProducer;
    @InjectMocks
    private AdvertServiceImpl advertService;

    @BeforeAll
    static void setUp() {
        advert = ObjectFactory.getInstance().getAdvert();
        advertList = ObjectFactory.getInstance().getAdvertList();
        advertSaveRequest = ObjectFactory.getInstance().getAdvertSaveRequest();
        advertUpdateRequest = ObjectFactory.getInstance().getAdvertUpdateRequest();
        advertStatusUpdateRequest = ObjectFactory.getInstance().getAdvertStatusUpdateRequest();
        userResponse = ObjectFactory.getInstance().getUserResponse();
        photoResponses = ObjectFactory.getInstance().getPhotoResponseList();
        addressResponse = ObjectFactory.getInstance().getAddressResponse();
    }

    @Test
    void givenAdvertSaveRequest_whenSave_thenAdvertShouldBeSaved() {
        when(advertRepository.save(any(Advert.class))).thenReturn(advert);
        when(userServiceClient.findById(advertSaveRequest.getUserId()))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, userResponse)));
        when(photoServiceClient.findByPhotoIds(any(PhotoIdsRequest.class)))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, photoResponses)));
        when(addressService.findById(advertSaveRequest.getAddressId()))
                .thenReturn(new SuccessDataResult<>(addressResponse, "Address found"));

        DataResult<AdvertResponse> result = advertService.save(advertSaveRequest);
        assertTrue(result.getSuccess(), "Advert should be saved successfully");
        assertNotNull(result.getData());

        verify(advertRepository, times(1)).save(any(Advert.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAdvertUpdateRequest_whenUpdate_thenAdvertShouldBeUpdated() {
        when(advertRepository.findById(advertUpdateRequest.getId())).thenReturn(Optional.of(advert));
        when(advertRepository.save(any(Advert.class))).thenReturn(advert);
        when(userServiceClient.findById(advertUpdateRequest.getUserId()))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, userResponse)));
        when(photoServiceClient.findByPhotoIds(any(PhotoIdsRequest.class)))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, photoResponses)));
        when(addressService.findById(advertUpdateRequest.getAddressId()))
                .thenReturn(new SuccessDataResult<>(addressResponse, "Address found"));

        DataResult<AdvertResponse> result = advertService.update(advertUpdateRequest);
        assertTrue(result.getSuccess(), "Advert should be updated successfully");

        verify(advertRepository, times(1)).findById(advertUpdateRequest.getId());
        verify(advertRepository, times(1)).save(any(Advert.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAdvertId_whenDeleteById_thenAdvertShouldBeDeleted() {
        when(advertRepository.findById(advert.getId())).thenReturn(Optional.of(advert));

        Result result = advertService.deleteById(advert.getId());
        assertTrue(result.getSuccess(), "Advert should be deleted successfully");

        verify(advertRepository, times(1)).deleteById(advert.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAdvertId_whenFindById_thenAdvertShouldBeFound() {
        when(advertRepository.findById(advert.getId())).thenReturn(Optional.of(advert));
        when(userServiceClient.findById(advert.getUserId()))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, userResponse)));
        when(photoServiceClient.findByPhotoIds(any(PhotoIdsRequest.class)))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, photoResponses)));

        DataResult<AdvertResponse> result = advertService.findById(advert.getId());
        assertTrue(result.getSuccess(), "Advert should be found successfully");

        verify(advertRepository, times(1)).findById(advert.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAdvertList_whenFindAll_thenAllAdvertsShouldBeReturnedd() {
        when(advertRepository.findAll()).thenReturn(advertList);
        when(userServiceClient.findById(any(Long.class)))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, userResponse)));
        when(photoServiceClient.findByPhotoIds(any(PhotoIdsRequest.class)))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, photoResponses)));

        DataResult<List<AdvertResponse>> result = advertService.findAll();
        assertTrue(result.getSuccess(), "All adverts should be returned successfully");
        assertEquals(advertList.size(), result.getData().size(), "Advert list size should match");

        verify(advertRepository, times(1)).findAll();
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentAdvertUpdateRequest_whenUpdate_thenAdvertNotFoundExceptionShouldBeThrown() {
        when(advertRepository.findById(advertUpdateRequest.getId())).thenReturn(Optional.empty());

        AdvertNotFoundException exception = assertThrows(AdvertNotFoundException.class,
                () -> advertService.update(advertUpdateRequest));
        assertEquals(ExceptionMessages.ADVERT_NOT_FOUND, exception.getMessage(), "Exception message should match");

        verify(advertRepository, times(1)).findById(advertUpdateRequest.getId());
        verify(advertRepository, never()).save(any(Advert.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentAdvertId_whenDeleteById_thenAdvertNotFoundExceptionShouldBeThrown() {
        when(advertRepository.findById(advert.getId())).thenReturn(Optional.empty());

        AdvertNotFoundException exception = assertThrows(AdvertNotFoundException.class,
                () -> advertService.deleteById(advert.getId()));
        assertEquals(ExceptionMessages.ADVERT_NOT_FOUND, exception.getMessage(), "Exception message should match");

        verify(advertRepository, times(1)).findById(advert.getId());
        verify(advertRepository, never()).deleteById(anyLong());
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentAdvertId_whenFindById_thenAdvertNotFoundExceptionShouldBeThrown() {
        when(advertRepository.findById(advert.getId())).thenReturn(Optional.empty());

        AdvertNotFoundException exception = assertThrows(AdvertNotFoundException.class,
                () -> advertService.findById(advert.getId()));
        assertEquals(ExceptionMessages.ADVERT_NOT_FOUND, exception.getMessage(), "Exception message should match");

        verify(advertRepository, times(1)).findById(advert.getId());
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAdvertSaveRequest_whenSave_withNullUserResponse_thenHandleNullUserResponse() {
        when(advertRepository.save(any(Advert.class))).thenReturn(advert);
        when(userServiceClient.findById(advertSaveRequest.getUserId()))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, null)));
        when(photoServiceClient.findByPhotoIds(any(PhotoIdsRequest.class)))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, photoResponses)));
        when(addressService.findById(advertSaveRequest.getAddressId()))
                .thenReturn(new SuccessDataResult<>(addressResponse, "Address found"));

        DataResult<AdvertResponse> result = advertService.save(advertSaveRequest);
        assertTrue(result.getSuccess(), "Advert should be saved successfully");
        assertNotNull(result.getData());

        verify(advertRepository, times(1)).save(any(Advert.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAdvertSaveRequest_whenSave_withNullPhotoResponses_thenHandleNullPhotoResponses() {
        when(advertRepository.save(any(Advert.class))).thenReturn(advert);
        when(userServiceClient.findById(advertSaveRequest.getUserId()))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, userResponse)));
        when(photoServiceClient.findByPhotoIds(any(PhotoIdsRequest.class)))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, null)));
        when(addressService.findById(advertSaveRequest.getAddressId()))
                .thenReturn(new SuccessDataResult<>(addressResponse, "Address found"));

        DataResult<AdvertResponse> result = advertService.save(advertSaveRequest);
        assertTrue(result.getSuccess(), "Advert should be saved successfully");
        assertNotNull(result.getData());

        verify(advertRepository, times(1)).save(any(Advert.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAdvertStatusUpdateRequest_whenChangeStatus_thenAdvertStatusShouldBeUpdated() {
        when(advertRepository.findById(advertStatusUpdateRequest.getId())).thenReturn(Optional.of(advert));
        when(advertRepository.save(any(Advert.class))).thenReturn(advert);
        when(userServiceClient.findById(advert.getUserId()))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, userResponse)));
        when(photoServiceClient.findByPhotoIds(any(PhotoIdsRequest.class)))
                .thenReturn(ResponseEntity.ok(new RestResponse<>(true, null, null, photoResponses)));

        DataResult<AdvertResponse> result = advertService.changeStatus(advertStatusUpdateRequest);
        assertTrue(result.getSuccess(), "Advert status should be updated successfully");
        assertEquals(AdvertStatus.ACTIVE, result.getData().getAdvertStatus(), "Advert status should match");

        verify(advertRepository, times(1)).findById(advertStatusUpdateRequest.getId());
        verify(advertRepository, times(1)).save(any(Advert.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }
}
