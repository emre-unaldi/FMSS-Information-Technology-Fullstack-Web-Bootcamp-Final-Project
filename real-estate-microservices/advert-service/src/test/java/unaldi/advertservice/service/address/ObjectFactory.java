package unaldi.advertservice.service.address;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import unaldi.advertservice.entity.Address;
import unaldi.advertservice.entity.dto.request.AddressSaveRequest;
import unaldi.advertservice.entity.dto.request.AddressUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectFactory {
    private static ObjectFactory instance;
    private Address address;
    private AddressSaveRequest addressSaveRequest;
    private AddressUpdateRequest addressUpdateRequest;
    private AddressResponse addressResponse;
    private List<Address> addressList;
    private List<AddressResponse> addressResponseList;

    public static synchronized ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    public Address getAddress() {
        if (address == null) {
            address = Address.builder()
                    .id(1L)
                    .neighborhood("Test Neighborhood")
                    .street("Test Street")
                    .province("Test Province")
                    .county("Test County")
                    .zipCode("12345")
                    .build();
        }
        return address;
    }

    public AddressSaveRequest getAddressSaveRequest() {
        if (addressSaveRequest == null) {
            addressSaveRequest = new AddressSaveRequest(
                    "Test Neighborhood",
                    "Test Street",
                    "Test Province",
                    "Test County",
                    "12345"
            );
        }
        return addressSaveRequest;
    }

    public AddressUpdateRequest getAddressUpdateRequest() {
        if (addressUpdateRequest == null) {
            addressUpdateRequest = new AddressUpdateRequest(
                    1L,
                    "Test Neighborhood",
                    "Test Street",
                    "Test Province",
                    "Test County",
                    "12345"
            );
        }
        return addressUpdateRequest;
    }

    public AddressResponse getAddressResponse() {
        if (addressResponse == null) {
            addressResponse = new AddressResponse(
                    1L,
                    "Test Neighborhood",
                    "Test Street",
                    "Test Province",
                    "Test County",
                    "12345"
            );
        }
        return addressResponse;
    }

    public List<Address> getAddressList() {
        if (addressList == null) {
            Address addressOne = Address.builder()
                    .id(1L)
                    .neighborhood("Neighborhood 1")
                    .street("Street 1")
                    .province("Province 1")
                    .county("County 1")
                    .zipCode("11111")
                    .build();

            Address addressTwo = Address.builder()
                    .id(2L)
                    .neighborhood("Neighborhood 2")
                    .street("Street 2")
                    .province("Province 2")
                    .county("County 2")
                    .zipCode("22222")
                    .build();

            addressList = List.of(addressOne, addressTwo);
        }
        return addressList;
    }

    public List<AddressResponse> getAddressResponseList() {
        if (addressResponseList == null) {
            AddressResponse responseOne = new AddressResponse(
                    1L,
                    "Neighborhood 1",
                    "Street 1",
                    "Province 1",
                    "County 1",
                    "11111"
            );

            AddressResponse responseTwo = new AddressResponse(
                    2L,
                    "Neighborhood 2",
                    "Street 2",
                    "Province 2",
                    "County 2",
                    "22222"
            );

            addressResponseList = List.of(responseOne, responseTwo);
        }
        return addressResponseList;
    }
}
