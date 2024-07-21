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
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertStatusUpdateRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AdvertResponse;
import unaldi.advertservice.service.AdvertService;
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
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/adverts")
@Tag(name = "Advert Controller", description = "Advert Management")
public class AdvertController {

    private final AdvertService advertService;

    @Autowired
    public AdvertController(AdvertService advertService) {
        this.advertService = advertService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new advert",
            description = "Create a new advert with the provided details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Advert details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AdvertSaveRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "New advert to save",
                                            summary = "New advert",
                                            description = "Complete request with all available fields for a new advert",
                                            value = "{\n"
                                                    + "  \"photoIds\": [\"21994444-1fa6-4344-806f-d78c2923511c\"],\n"
                                                    + "  \"userId\": 1,\n"
                                                    + "  \"housingType\": \"SUMMER_HOUSE\",\n"
                                                    + "  \"advertType\": \"FOR_SALE\",\n"
                                                    + "  \"title\": \"Sample Apartment Listing\",\n"
                                                    + "  \"description\": \"Spacious apartment with great amenities\",\n"
                                                    + "  \"addressId\": 1,\n"
                                                    + "  \"releaseDate\": \"2024-07-16\",\n"
                                                    + "  \"validityDate\": \"2024-12-18\",\n"
                                                    + "  \"area\": 1400,\n"
                                                    + "  \"numberOfRooms\": 19,\n"
                                                    + "  \"price\": 220000,\n"
                                                    + "  \"isBalcony\": true,\n"
                                                    + "  \"isCarPark\": false\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<AdvertResponse>> save(@Valid @RequestBody AdvertSaveRequest advertSaveRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(advertService.save(advertSaveRequest));
    }

    @PutMapping
    @Operation(
            summary = "Update an existing advert",
            description = "Update an existing advert with the provided details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Advert details including ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AdvertUpdateRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "Advert",
                                            summary = "Update",
                                            description = "Update the information of an existing advert",
                                            value = "{\n"
                                                    + "  \"id\": 1,\n"
                                                    + "  \"photoIds\": [\"21994444-1fa6-4344-806f-d78c2923511c\"],\n"
                                                    + "  \"userId\": 1,\n"
                                                    + "  \"housingType\": \"VILLA\",\n"
                                                    + "  \"advertStatus\": \"ACTIVE\",\n"
                                                    + "  \"advertType\": \"FOR_SALE\",\n"
                                                    + "  \"title\": \"Sample Apartment Listing\",\n"
                                                    + "  \"description\": \"Spacious apartment with great amenities\",\n"
                                                    + "  \"addressId\": 1,\n"
                                                    + "  \"releaseDate\": \"2024-07-15\",\n"
                                                    + "  \"validityDate\": \"2024-09-12\",\n"
                                                    + "  \"area\": 550,\n"
                                                    + "  \"numberOfRooms\": 3,\n"
                                                    + "  \"price\": 5000,\n"
                                                    + "  \"isBalcony\": true,\n"
                                                    + "  \"isCarPark\": false\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<AdvertResponse>> update(@Valid @RequestBody AdvertUpdateRequest advertUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertService.update(advertUpdateRequest));
    }

    @GetMapping
    @Operation(
            summary = "Find all adverts",
            description = "Retrieve a list of all adverts",
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Adverts Infos",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<List<AdvertResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertService.findAll());
    }

    @GetMapping("/{advertId}")
    @Operation(
            summary = "Find advert by id",
            description = "Retrieve details of an advert by its id",
            parameters = {
                    @Parameter(
                            name = "advertId",
                            description = "Id of the advert to retrieve",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Advert id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<AdvertResponse>> findById(@PathVariable("advertId") Long advertId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertService.findById(advertId));
    }

    @DeleteMapping("/{advertId}")
    @Operation(
            summary = "Delete advert by id",
            description = "Delete an advert by its id",
            parameters = {
                    @Parameter(
                            name = "advertId",
                            description = "Id of the advert to delete",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Advert id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<Result> deleteById(@PathVariable("advertId") Long advertId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertService.deleteById(advertId));
    }

    @PatchMapping("/changeStatus")
    @Operation(
            summary = "Change advert status",
            description = "Change the status of an advert identified by id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Advert status update details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AdvertStatusUpdateRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "Advert Status Update",
                                            summary = "Update advert status",
                                            description = "Update the status of an existing advert",
                                            value = "{\n"
                                                    + "  \"id\": 1,\n"
                                                    + "  \"advertStatus\": \"ACTIVE\"\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<AdvertResponse>> changeStatus(@Valid @RequestBody AdvertStatusUpdateRequest advertStatusUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertService.changeStatus(advertStatusUpdateRequest));
    }

}
