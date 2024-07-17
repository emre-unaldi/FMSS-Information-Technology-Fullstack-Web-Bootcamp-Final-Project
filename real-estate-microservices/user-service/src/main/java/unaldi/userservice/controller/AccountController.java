package unaldi.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.userservice.entity.dto.response.AccountResponse;
import unaldi.userservice.service.AccountService;
import unaldi.userservice.utils.result.DataResult;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 17.07.2024
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name="Account Controller", description = "Account Management")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @Operation(
            description = "Find all accounts",
            summary = "Find all",
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Account Infos",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<List<AccountResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.findAll());
    }

    @GetMapping("/{accountId}")
    @Operation(
            description = "Find with id a account",
            summary = "Find by id",
            parameters = {
                    @Parameter(
                            name = "accountId",
                            description = "Id of the account to retrieve",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Account id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<AccountResponse>> findById(@PathVariable("accountId") Long accountId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.findById(accountId));
    }

}
