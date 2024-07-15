package unaldi.advertservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.advertservice.entity.dto.request.AddressSaveRequest;
import unaldi.advertservice.entity.dto.request.AddressUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;
import unaldi.advertservice.service.AddressService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/addresses")
@Tag(name = "Address Controller", description = "Address Management")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new address",
            description = "Create a new address with the provided details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Address details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AddressSaveRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "New address to save",
                                            summary = "New address",
                                            description = "Complete request with all available fields for a new address",
                                            value = "{\n"
                                                    + "  \"neighborhood\": \"West End\",\n"
                                                    + "  \"street\": \"202 Cedar Road\",\n"
                                                    + "  \"province\": \"Nova Scotia\",\n"
                                                    + "  \"county\": \"Halifax\",\n"
                                                    + "  \"zipCode\": \"B3H1Z72\"\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<AddressResponse>> save(@Valid @RequestBody AddressSaveRequest addressSaveRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addressService.save(addressSaveRequest));
    }

    @PutMapping
    @Operation(
            summary = "Update an existing address",
            description = "Update an existing address with the provided details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Address details including ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AddressUpdateRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "Address",
                                            summary = "Update",
                                            description = "Update the information of an existing address",
                                            value = "{\n"
                                                    + "  \"id\": 1,\n"
                                                    + "  \"neighborhood\": \"West End\",\n"
                                                    + "  \"street\": \"202 Cedar Road\",\n"
                                                    + "  \"province\": \"Nova Scotia\",\n"
                                                    + "  \"county\": \"Halifax\",\n"
                                                    + "  \"zipCode\": \"B3H1Z7\"\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<AddressResponse>> update(@Valid @RequestBody AddressUpdateRequest addressUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressService.update(addressUpdateRequest));
    }

    @GetMapping
    @Operation(
            summary = "Find all addresses",
            description = "Retrieve a list of all addresses",
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Addresses Infos",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<List<AddressResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressService.findAll());
    }

    @GetMapping("/{addressId}")
    @Operation(
            summary = "Find address by ID",
            description = "Retrieve details of an address by its ID",
            parameters = {
                    @Parameter(
                            name = "addressId",
                            description = "Id of the address to retrieve",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Address id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<AddressResponse>> findById(@PathVariable("addressId") Long addressId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressService.findById(addressId));
    }

    @DeleteMapping("/{addressId}")

    @Operation(
            summary = "Delete address by id",
            description = "Delete an address by its id",
            parameters = {
                    @Parameter(
                            name = "addressId",
                            description = "Id of the address to delete",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Address Id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<Result> deleteById(@PathVariable("addressId") Long addressId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressService.deleteById(addressId));
    }

}
