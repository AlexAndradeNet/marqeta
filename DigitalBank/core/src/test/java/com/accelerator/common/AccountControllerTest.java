/* BlankFactor (C)2023 */
package com.accelerator.common;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.accelerator.BaseTest;
import com.accelerator.common.dto.request.AccountRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

class AccountControllerTest extends BaseTest {
    private static final String ENDPOINT = "/port/account/create";
    private static final String VALID_GUID = "12345678-1234-1234-1234-123456789012";
    private AccountRequest requestPayload;

    @BeforeEach
    public void init() {
        requestPayload = new AccountRequest();
        requestPayload.setCardId(VALID_GUID);
        requestPayload.setAccountBalance(new BigDecimal(10000));
        requestPayload.setReservationBalance(BigDecimal.ZERO);
    }

    @Test
    void shouldSuccessfullyCreateAccount() throws Exception {
        performPostRequestAndExpectStatus(requestPayload, status().isCreated())
                .andExpect(content().string("{\"accountId\":1,\"cardId\":\"" + VALID_GUID + "\"}"));
        checkAccountBalance(10000, 0);
    }

    @Test
    void shouldFailWhenCardIdIsInvalid() throws Exception {
        requestPayload.setCardId("not-a-guid");
        String expectedResponse =
                "{\"message\":\"Validation error: cardId must be a valid GUID\",\"status\":400}";

        performPostRequestAndExpectStatus(requestPayload, status().isBadRequest())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void shouldFailWhenCardIdIsMissing() throws Exception {
        requestPayload.setCardId("");

        performPostRequestAndExpectStatus(requestPayload, status().isBadRequest())
                .andExpect(content().string(containsString("cardId is required")));
    }

    @Test
    void shouldFailWhenAccountBalanceIsZero() throws Exception {
        requestPayload.setAccountBalance(BigDecimal.ZERO);

        performPostRequestAndExpectStatus(requestPayload, status().isBadRequest())
                .andExpect(content().string(containsString("amount should be greater than zero")));
    }

    // AUXILIARY METHODS

    private ResultActions performPostRequestAndExpectStatus(
            AccountRequest payload, ResultMatcher statusMatcher) throws Exception {
        return mockMvc.perform(
                        post(ENDPOINT)
                                .contentType(CONTENT_TYPE)
                                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(statusMatcher)
                .andExpect(header().string("content-type", CONTENT_TYPE));
    }
}
