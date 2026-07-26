/* BlankFactor (C)2023 */
package com.accelerator.authorization;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.accelerator.BaseTest;
import com.accelerator.authorization.dto.request.AuthorizationRequest;
import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.Transaction;
import com.accelerator.ledger.dto.response.LedgerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class AuthorizationGatewayControllerTest extends BaseTest {
    private static final String ENDPOINT = "/port/authorization-gateway";
    private static final String INVALID_TRANSACTION_TYPE = "INVALID_TRANSACTION_TYPE";
    private static final String INVALID_TRANSACTION_STATUS = "INVALID_TRANSACTION_STATUS";
    private static AuthorizationRequest requestPayload;
    private static String payload;

    @BeforeEach
    public void initLocal() throws IOException {
        setupPayload();
        setupAccount();
        setupExpectedAccount();
        setupExpectedTransaction();
    }

    @Test
    void shouldAuthorizeSuccessfullyWhenValidRequest() throws Exception {
        startLedgerMockServer();
        setLedgerMockResponseToSuccess();
        saveAccountChangesIntoDB();
        createTransitAccountForClearingTransaction();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"OK\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 0);
        checkTransitAccountBalance(0, 0);

        performPostRequestAndExpect(expectedResponsePayload, HttpStatus.OK.value());

        checkAccountBalance(0, 100);
        checkTransitAccountBalance(0, 0); // Expecting no changes in the transit account balance
        checkForTransactionRecorded();
    }

    @Test
    void shouldAuthorizeSuccessfullyWhenAmountIsLessThanBalance() throws Exception {
        startLedgerMockServer();
        setLedgerMockResponseToSuccess();
        requestPayload.setAmount(new BigDecimal("98.00"));
        expectedAccount.setReservationBalance(new BigDecimal("98.00"));
        expectedAccount.setAccountBalance(new BigDecimal("2.00"));
        saveAccountChangesIntoDB();
        createTransitAccountForClearingTransaction();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"OK\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 0);
        checkTransitAccountBalance(0, 0);

        performPostRequestAndExpect(expectedResponsePayload, HttpStatus.OK.value());

        checkAccountBalance(2, 98);
        expectedTransaction.setAmount(new BigDecimal("98.00"));
        checkTransitAccountBalance(0, 0); // Expecting no changes in the transit account balance
        checkForTransactionRecorded();
    }

    @Test
    void shouldFailAuthorizationForInvalidCard() throws Exception {
        requestPayload.setCardToken("NON CARD");
        saveAccountChangesIntoDB();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"INVALID_CARD\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 0);

        performPostRequestAndExpect(expectedResponsePayload, HttpStatus.BAD_REQUEST.value());

        checkAccountBalance(100, 0); // Expecting no changes in the account balance
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldFailAuthorizationForInsufficientFunds() throws Exception {
        account.setAccountBalance(new BigDecimal(0));
        saveAccountChangesIntoDB();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"INSUFFICIENT_FUNDS\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(0, 0);

        performPostRequestAndExpect(expectedResponsePayload, HttpStatus.BAD_REQUEST.value());

        checkAccountBalance(0, 0); // Expecting no balance changes
        // Expecting transaction recorded with status DECLINED
        expectedTransaction.setTransactionStatus("DECLINED");
        expectedTransaction.getAccount().setAccountBalance(new BigDecimal("0.00"));
        expectedTransaction.getAccount().setReservationBalance(new BigDecimal("0.00"));
        checkForTransactionRecorded();
    }

    @Test
    void shouldFailAuthorizationForAnInvalidTransactionType() throws Exception {
        requestPayload.setType("invalid");
        saveAccountChangesIntoDB();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"%s\"}",
                        requestPayload.getTransactionToken(), INVALID_TRANSACTION_TYPE);

        checkAccountBalance(100, 0);

        performPostRequestAndExpect(expectedResponsePayload, HttpStatus.BAD_REQUEST.value());

        checkAccountBalance(100, 0);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldFailAuthorizationForANonPermittedTransactionType() throws Exception {
        requestPayload.setType("authorization.clearing");
        saveAccountChangesIntoDB();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"%s\"}",
                        requestPayload.getTransactionToken(), "TRANSACTION_NOT_PERMITTED");

        checkAccountBalance(100, 0);

        performPostRequestAndExpect(expectedResponsePayload, HttpStatus.BAD_REQUEST.value());

        checkAccountBalance(100, 0);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldFailAuthorizationForTransactionTypeNotExistent() throws Exception {
        requestPayload.setType("wrong");
        saveAccountChangesIntoDB(); // Setup database
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"INVALID_TRANSACTION_TYPE\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 0);

        performPostRequestAndExpect(expectedResponsePayload, HttpStatus.BAD_REQUEST.value());

        checkAccountBalance(100, 0); // Expecting no changes in the account balance
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldFailAuthorizationForStatusNotExistent() throws Exception {
        requestPayload.setStatus("WRONG");
        saveAccountChangesIntoDB();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"%s\"}",
                        requestPayload.getTransactionToken(), INVALID_TRANSACTION_STATUS);

        checkAccountBalance(100, 0);

        performPostRequestAndExpect(expectedResponsePayload, HttpStatus.BAD_REQUEST.value());

        checkAccountBalance(100, 0); // Expecting no changes in the account balance
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldAuthorizeFailsWhenThereIsNotATransitAccount() throws Exception {
        saveAccountChangesIntoDB();
        // TODO: the response needs the token property
        String expectedResponsePayload =
                String.format(
                        "{\"message\":\"Transit account not found\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 0);

        performPostRequestAndExpect(
                expectedResponsePayload, HttpStatus.INTERNAL_SERVER_ERROR.value());

        checkAccountBalance(100, 0); // Expecting no changes in the account balance
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldAuthorizeFailsWhenLegerHasNetworkingIssues() throws Exception {
        startLedgerMockServer();
        setLedgerMockResponseToNetworkFailure();
        saveAccountChangesIntoDB();
        createTransitAccountForClearingTransaction();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"NULL\", \"message\":\"Invalid Ledger response\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 0);
        checkTransitAccountBalance(0, 0);

        performPostRequestAndExpect(
                expectedResponsePayload, HttpStatus.INTERNAL_SERVER_ERROR.value());

        checkAccountBalance(100, 0); // Expecting no changes in the account balance
        checkTransitAccountBalance(0, 0); // Expecting no changes in the transit account balance
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldAuthorizeFailsWhenLegerResponseEmpty() throws Exception {
        startLedgerMockServer();
        setLedgerMockResponse("", HttpStatus.OK);
        saveAccountChangesIntoDB();
        createTransitAccountForClearingTransaction();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"NULL\", \"message\":\"Invalid Ledger response\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 0);
        checkTransitAccountBalance(0, 0);

        performPostRequestAndExpect(
                expectedResponsePayload, HttpStatus.INTERNAL_SERVER_ERROR.value());

        checkAccountBalance(100, 0); // Expecting no changes in the account balance
        checkTransitAccountBalance(0, 0); // Expecting no changes in the transit account balance
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldAuthorizeFailsWhenLegerResponseHasEmptyFields() throws Exception {
        LedgerResponse ledgerResponse =
                objectMapper.readValue(LEDGER_SUCCESS_RESPONSE, LedgerResponse.class);
        ledgerResponse.setReservationId(null);
        startLedgerMockServer();
        setLedgerMockResponse(objectMapper.writeValueAsString(ledgerResponse), HttpStatus.OK);
        saveAccountChangesIntoDB();
        createTransitAccountForClearingTransaction();
        String expectedResponsePayload =
                "{\"token\":\"NULL\", \"message\":\"Invalid Ledger response\"}";

        checkAccountBalance(100, 0);
        checkTransitAccountBalance(0, 0);

        performPostRequestAndExpect(
                expectedResponsePayload, HttpStatus.INTERNAL_SERVER_ERROR.value());

        checkAccountBalance(100, 0); // Expecting no changes in the account balance
        checkTransitAccountBalance(0, 0); // Expecting no changes in the transit account balance
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldAuthorizeFailsWhenLegerResponseWithBadStatus() throws Exception {
        startLedgerMockServer();
        setLedgerMockResponse(LEDGER_SUCCESS_RESPONSE, HttpStatus.BAD_REQUEST);
        saveAccountChangesIntoDB();
        createTransitAccountForClearingTransaction();
        String expectedResponsePayload =
                String.format(
                        "{\"token\":\"NULL\", \"message\":\"Invalid Ledger response\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 0);
        checkTransitAccountBalance(0, 0);

        performPostRequestAndExpect(
                expectedResponsePayload, HttpStatus.INTERNAL_SERVER_ERROR.value());

        checkAccountBalance(100, 0); // Expecting no changes in the account balance
        checkTransitAccountBalance(0, 0); // Expecting no changes in the transit account balance
        checkForNoTransactionRecorded();
    }

    // AUXILIARY METHODS

    private void setupPayload() throws JsonProcessingException {
        payload =
                "{\"transactionToken\":\"tx_1\",\"amount\":100,\"type\":\"authorization\",\"status\":\"PENDING\",\"userToken\":\"userToken\",\"cardToken\":\"1003\",\"payload\":\"json"
                    + " payload\", \"currency\": \"USD\", \"timestamp\":"
                    + " \"2021-08-01T12:00:00.000Z\", \"details\": \"detailsExample\", \"scheme\":"
                    + " \"schemeExample\", \"merchantName\": \"BlankFactor\", \"merchantCity\":"
                    + " \"Medellin\", \"merchantCountry\": \"USA\"}";
        requestPayload = objectMapper.readValue(payload, AuthorizationRequest.class);
        requestPayload.setTransactionToken(UUID.randomUUID().toString());
        requestPayload.setCardToken(UUID.randomUUID().toString());
        requestPayload.setPayload(payload);
    }

    private void setupAccount() {
        account = new Account();
        account.setAccountId(null);
        account.setUserId("4444L");
        account.setCardId(requestPayload.getCardToken());
        account.setAccountBalance(new BigDecimal("100.00"));
        account.setReservationBalance(new BigDecimal("0.00"));
    }

    private void setupExpectedAccount() {
        expectedAccount = new Account();
        expectedAccount.setAccountId(1L);
        expectedAccount.setUserId(account.getUserId());
        expectedAccount.setCardId(account.getCardId());
        expectedAccount.setAccountBalance(
                formatBigDecimal(account.getAccountBalance().subtract(requestPayload.getAmount())));
        expectedAccount.setReservationBalance(formatBigDecimal(requestPayload.getAmount()));
    }

    private void setupExpectedTransaction() {
        expectedTransaction = new Transaction();
        expectedTransaction.setTransactionId(1L);
        expectedTransaction.setTransactionToken(requestPayload.getTransactionToken());
        expectedTransaction.setTransactionType(requestPayload.getType());
        expectedTransaction.setAccount(expectedAccount);
        expectedTransaction.setAmount(
                formatBigDecimal(new BigDecimal(String.valueOf(requestPayload.getAmount()))));
        expectedTransaction.setTransactionStatus(requestPayload.getStatus());
        expectedTransaction.setPayload(payload);
    }

    private void performPostRequestAndExpect(String expectedResponsePayload, int expectedStatus)
            throws Exception {
        mockMvc.perform(
                        post(ENDPOINT)
                                .contentType(CONTENT_TYPE)
                                .content(objectMapper.writeValueAsString(requestPayload)))
                .andExpect(status().is(expectedStatus))
                .andExpect(header().string("content-type", CONTENT_TYPE))
                .andExpect(content().json(expectedResponsePayload));
    }
}
