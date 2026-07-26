/* BlankFactor (C)2023 */
package com.accelerator.common.controller;

import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.request.AccountRequest;
import com.accelerator.common.dto.response.AccountResponse;
import com.accelerator.common.port.CreateAccountPort;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/port/account/create")
public class AccountController {

    private static final String CONTROLLER = "AccountController";
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final CreateAccountPort accountPort;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody @Valid AccountRequest accountRequest)
            throws URISyntaxException, ValidationException {

        logger.info("%s: %s".formatted(CONTROLLER, accountRequest.toString()));

        Account newAccount = accountPort.createAccount(accountRequest);

        return ResponseEntity.created(new URI(""))
                .body(
                        AccountResponse.builder()
                                .accountId(newAccount.getAccountId())
                                .cardId(accountRequest.getCardId())
                                .build());
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class,
        ValidationException.class
    })
    @ResponseBody
    public ResponseEntity<Object> handleBadRequest(Exception ex) {
        logger.error(CONTROLLER + ": " + ex.getMessage(), ex);
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation error: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
