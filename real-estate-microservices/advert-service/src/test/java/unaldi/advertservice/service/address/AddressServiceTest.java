package unaldi.advertservice.service.address;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import unaldi.advertservice.entity.Address;
import unaldi.advertservice.entity.dto.request.AddressSaveRequest;
import unaldi.advertservice.entity.dto.request.AddressUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;
import unaldi.advertservice.repository.AddressRepository;
import unaldi.advertservice.repository.AdvertRepository;
import unaldi.advertservice.service.Impl.AddressServiceImpl;
import unaldi.advertservice.utils.exception.AddressNotFoundException;
import unaldi.advertservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.advertservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.advertservice.utils.result.DataResult;
import unaldi.advertservice.utils.result.Result;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    private static Address address;
    private static List<Address> addressList;
    private static AddressSaveRequest addressSaveRequest;
    private static AddressUpdateRequest addressUpdateRequest;
    private static AddressResponse addressResponse;
    private static List<AddressResponse> addressResponseList;
    private final Long nonExistentAddressId = -1L;

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AdvertRepository advertRepository;
    @Mock
    private LogProducer logProducer;
    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeAll
    static void setUp() {
        address = ObjectFactory.getInstance().getAddress();
        addressList = ObjectFactory.getInstance().getAddressList();
        addressSaveRequest = ObjectFactory.getInstance().getAddressSaveRequest();
        addressUpdateRequest = ObjectFactory.getInstance().getAddressUpdateRequest();
        addressResponse = ObjectFactory.getInstance().getAddressResponse();
        addressResponseList = ObjectFactory.getInstance().getAddressResponseList();
    }

    @Test
    void givenAddressSaveRequest_whenSave_thenAddressShouldBeSaved() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        DataResult<AddressResponse> result = addressService.save(addressSaveRequest);
        assertTrue(result.getSuccess(), "Address should be saved");

        verify(addressRepository, times(1)).save(any(Address.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAddressUpdateRequest_whenUpdate_thenAddressShouldBeUpdated() {
        when(addressRepository.existsById(addressUpdateRequest.getId())).thenReturn(true);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        DataResult<AddressResponse> result = addressService.update(addressUpdateRequest);
        assertTrue(result.getSuccess(), "Address should be updated");
        assertEquals(addressResponse, result.getData(), "Updated address should match expected response");

        verify(addressRepository, times(1)).existsById(anyLong());
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAddressId_whenDeleteById_thenAddressShouldBeDeleted() {
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        when(advertRepository.existsByAddress(address.getId())).thenReturn(false);

        Result result = addressService.deleteById(address.getId());
        assertTrue(result.getSuccess(), "Address should be deleted");

        verify(addressRepository, times(1)).deleteById(address.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAddressId_whenFindById_thenAddressShouldBeFound() {
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));

        DataResult<AddressResponse> result = addressService.findById(address.getId());
        assertTrue(result.getSuccess(), "Address should be found");
        assertEquals(addressResponse, result.getData(), "Found address should match expected response");

        verify(addressRepository, times(1)).findById(address.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenAddressList_whenFindAll_thenAllAddressesShouldBeReturned() {
        when(addressRepository.findAll()).thenReturn(addressList);

        DataResult<List<AddressResponse>> result = addressService.findAll();
        assertTrue(result.getSuccess(), "All addresses should be returned");
        assertEquals(addressResponseList, result.getData(), "Returned addresses should match expected response");

        verify(addressRepository, times(1)).findAll();
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentAddressUpdateRequest_whenUpdate_thenAddressNotFoundExceptionShouldBeThrown() {
        when(addressRepository.existsById(addressUpdateRequest.getId())).thenReturn(false);

        assertThrows(AddressNotFoundException.class, () -> {
            addressService.update(addressUpdateRequest);
        }, "AddressNotFoundException should be thrown");

        verify(addressRepository, times(1)).existsById(anyLong());
        verify(addressRepository, never()).save(any(Address.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentAddressId_whenDeleteById_thenAddressNotFoundExceptionShouldBeThrown() {
        when(addressRepository.findById(nonExistentAddressId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> {
            addressService.deleteById(nonExistentAddressId);
        }, "AddressNotFoundException should be thrown");

        verify(addressRepository, times(1)).findById(nonExistentAddressId);
        verify(addressRepository, never()).deleteById(anyLong());
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentAddressId_whenFindById_thenAddressNotFoundExceptionShouldBeThrown() {
        when(addressRepository.findById(nonExistentAddressId)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> {
            addressService.findById(nonExistentAddressId);
        }, "AddressNotFoundException should be thrown");

        verify(addressRepository, times(1)).findById(nonExistentAddressId);
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }
}
