/* BlankFactor (C)2023 */
package com.accelerator.authorization;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.accelerator.BaseTest;
import com.accelerator.authorization.dto.request.AuthorizationRequest;
import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.Transaction;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

class AuthorizationWebhookControllerTest extends BaseTest {
    private static final String ENDPOINT_CLEARING = "/port/authorization-webhook/clearing";
    private static final String ENDPOINT_REVERSAL = "/port/authorization-webhook/reversal";
    private static AuthorizationRequest requestPayload;
    private static Transaction precedingTransaction;
    private String payload;

    @BeforeEach
    public void init() throws IOException {
        setupPayload();
        setupAccount();
        startLedgerMockServer();
        setLedgerMockResponseToSuccess();
        setupExpectedAccount();
        setupPrecedingTransaction();
        setupExpectedTransaction();
    }

    @Test
    void shouldClearingSuccessWhenEverythingIsOk() throws Exception {
        createTransitAccountForClearingTransaction();
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        setRequestPayloadForClearing();
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"OK\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);

        performPostRequestAndExpect(ENDPOINT_CLEARING, expectedResponse, status().isOk());

        // Expecting the account balance to be updated
        checkAccountBalance(100, 0);
        checkTransitAccountBalance(10, 0);
        checkForTransactionRecorded();
    }

    @Test
    void shouldClearingFailsWhenThePrecedingTokenDoesNotExists() throws Exception {
        saveAccountChangesIntoDB();
        createTransitAccountForClearingTransaction();
        createPrecedingTransaction();
        setRequestPayloadForClearing();
        requestPayload.setPrecedingTransactionToken("non-existent-token");
        // TODO: answer needs the 'token' property
        String expectedResponse =
                String.format(
                        "{\"token\":\"NULL\",\"message\":\"PRECEDING_TRANSACTION_TOKEN_NOT_FOUND\"}");

        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);

        // TODO: Code should be 404 = not found
        performPostRequestAndExpect(ENDPOINT_CLEARING, expectedResponse, status().isBadRequest());

        // Expecting the account balance not to be updated
        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldClearingSuccessIdempotencyWhenTheSameTransactionIsRequestedTwice() throws Exception {
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        createTransitAccountForClearingTransaction();
        setRequestPayloadForClearing();
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"OK\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);

        // Perform first POST request
        performPostRequestAndExpect(ENDPOINT_CLEARING, expectedResponse, status().isOk());

        // Expecting the account is updated
        checkAccountBalance(100, 0);
        checkTransitAccountBalance(10, 0);
        checkForTransactionRecorded(); // transaction already saved.

        // Perform a second POST request for test idempotency
        performPostRequestAndExpect(ENDPOINT_CLEARING, expectedResponse, status().isOk());

        // Expecting the account is not updated again
        checkAccountBalance(100, 0);
        checkTransitAccountBalance(10, 0);
        checkForTransactionRecorded(); // transaction already saved.
    }

    @Test
    void shouldClearingFailsWhenTheTransitAccountDoesNotExists() throws Exception {
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        setRequestPayloadForClearing();
        // Do not save special transit account for clearing
        String expectedResponse =
                String.format(
                        "{\"token\":\"NULL\",\"message\":\"Transit account not found\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);

        performPostRequestAndExpect(
                ENDPOINT_CLEARING, expectedResponse, status().isInternalServerError());

        checkAccountBalance(100, 10); // Expecting no changes in the account balance
        checkAccountClearingDoesNotExists(); // Check the clearing account is not created for error
        checkForNoTransactionRecorded(); // Expecting no transaction recorded
    }

    @Test
    void shouldReversalSuccessWhenEverythingIsOk() throws Exception {
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        setRequestPayloadForReversal();

        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"OK\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);

        performPostRequestAndExpect(ENDPOINT_REVERSAL, expectedResponse, status().isOk());

        checkAccountBalance(110, 0); // Expecting the account is updated
        checkForTransactionRecorded(); // Expecting the transaction is recorded
    }

    @Test
    void shouldReversalFailsWhenRequestBodyIsEmpty() throws Exception {
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        setRequestPayloadForReversal();
        requestPayload = null;

        checkAccountBalance(100, 10);

        performPostRequestAndExpectForPartialText(
                ENDPOINT_REVERSAL, "Required request body is missing");

        // Expecting the account balance to not be updated
        checkAccountBalance(100, 10);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldReversalFailsWhenTheProvidedPrecedingTransactionTokenDoesNotExistsInTheDB()
            throws Exception {
        saveAccountChangesIntoDB();
        // Do not save a previous/preceding transaction
        setRequestPayloadForReversal();
        // TODO: answer needs the 'token' property
        String expectedResponse =
                String.format(
                        "{\"token\":\"NULL\",\"message\":\"PRECEDING_TRANSACTION_TOKEN_NOT_FOUND\"}");

        checkAccountBalance(100, 10);

        // TODO: Code should be 404 = not found
        performPostRequestAndExpect(ENDPOINT_REVERSAL, expectedResponse, status().isBadRequest());

        // Expecting the account balance to be updated
        checkAccountBalance(100, 10);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldReversalFailsWhenRequestAnInvalidTransaction() throws Exception {
        saveAccountChangesIntoDB();
        setRequestPayloadForReversal();
        requestPayload.setType("not-permitted-transaction");
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"INVALID_TRANSACTION_TYPE\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);

        performPostRequestAndExpect(ENDPOINT_REVERSAL, expectedResponse, status().isBadRequest());

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldReversalFailsWhenRequestANonPermittedTransaction() throws Exception {
        saveAccountChangesIntoDB();
        setRequestPayloadForReversal();
        requestPayload.setType("authorization.clearing");
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"TRANSACTION_NOT_PERMITTED\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);

        performPostRequestAndExpect(ENDPOINT_REVERSAL, expectedResponse, status().isBadRequest());

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldClearingFailsWhenRequestANonPermittedTransaction() throws Exception {
        saveAccountChangesIntoDB();
        setRequestPayloadForClearing();
        requestPayload.setType("authorization.reversal");
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"TRANSACTION_NOT_PERMITTED\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);

        performPostRequestAndExpect(ENDPOINT_CLEARING, expectedResponse, status().isBadRequest());

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldReversalFailsWhenPayloadCardAndAccountCardMismatch() throws Exception {
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        setRequestPayloadForReversal();
        requestPayload.setCardToken(
                "invalid-card-token"); // Force a mismatch between the card token and the account
        // TODO: improve text to reflect the mismatch
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"INVALID_CARD\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);

        // TODO: Code should be 404 = not found
        performPostRequestAndExpect(ENDPOINT_REVERSAL, expectedResponse, status().isBadRequest());

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldClearingFailsWhenPayloadCardAndAccountCardMismatch() throws Exception {
        createTransitAccountForClearingTransaction();
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        setRequestPayloadForClearing();
        requestPayload.setCardToken(
                "invalid-card-token"); // Force a mismatch between the card token and the account
        // TODO: improve text to reflect the mismatch
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"INVALID_CARD\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);

        // TODO: Code should be 404 = not found
        performPostRequestAndExpect(ENDPOINT_CLEARING, expectedResponse, status().isBadRequest());

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldReversalFailsWhenInsufficientFunds() throws Exception {
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        setRequestPayloadForReversal();
        requestPayload.setAmount(precedingTransaction.getAmount().add(BigDecimal.ONE));
        // TODO: answer needs the 'token' property
        String expectedResponse =
                String.format(
                        "{\"token\":\"NULL\",\"message\":\"INSUFFICIENT_FUNDS\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);

        // TODO: Should be 402 = payment required (or funds are required)
        performPostRequestAndExpect(ENDPOINT_REVERSAL, expectedResponse, status().isBadRequest());

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldClearingFailsWheInsufficientFunds() throws Exception {
        saveAccountChangesIntoDB();
        createTransitAccountForClearingTransaction();
        createPrecedingTransaction();
        setRequestPayloadForClearing();
        requestPayload.setAmount(precedingTransaction.getAmount().add(BigDecimal.ONE));
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"INSUFFICIENT_FUNDS\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);

        // TODO: Should be 402 = payment required (or funds are required)
        performPostRequestAndExpect(ENDPOINT_CLEARING, expectedResponse, status().isBadRequest());

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldReversalSuccessIdempotencyWhenTheSameTransactionIsRequestedTwice() throws Exception {
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        createTransitAccountForClearingTransaction();
        setRequestPayloadForReversal();
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"OK\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);

        // Perform first POST request
        performPostRequestAndExpect(ENDPOINT_REVERSAL, expectedResponse, status().isOk());

        // Expecting the account is updated
        checkAccountBalance(110, 0);
        checkTransitAccountBalance(0, 0);
        checkForTransactionRecorded(); // transaction already saved.

        // Perform a second POST request for test idempotency
        performPostRequestAndExpect(ENDPOINT_REVERSAL, expectedResponse, status().isOk());
        // Expecting the account is not updated
        checkAccountBalance(110, 0);
        checkTransitAccountBalance(0, 0);
        checkForTransactionRecorded(); // transaction already saved.
    }

    @Test
    void shouldReversalFailsWhenRequestAnInvalidPrecedingTransactionToken() throws Exception {
        setRequestPayloadForReversal();
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        requestPayload.setPrecedingTransactionToken(null);
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"INVALID_PRECEDING_TRANSACTION_TOKEN\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);

        performPostRequestAndExpect(ENDPOINT_REVERSAL, expectedResponse, status().isBadRequest());

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldReversalFailsWhenRequestAnInvalidCardToken() throws Exception {
        setRequestPayloadForReversal();
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        requestPayload.setCardToken("");

        checkAccountBalance(100, 10);

        performPostRequestAndExpectForPartialText(ENDPOINT_REVERSAL, "cardToken is required");

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkForNoTransactionRecorded();
    }

    @Test
    void shouldClearingFailsWhenRequestAnInvalidPrecedingTransactionToken() throws Exception {
        setRequestPayloadForClearing();
        createTransitAccountForClearingTransaction();
        saveAccountChangesIntoDB();
        createPrecedingTransaction();
        requestPayload.setPrecedingTransactionToken(null);
        String expectedResponse =
                String.format(
                        "{\"token\":\"%s\",\"message\":\"INVALID_PRECEDING_TRANSACTION_TOKEN\"}",
                        requestPayload.getTransactionToken());

        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);

        performPostRequestAndExpect(ENDPOINT_CLEARING, expectedResponse, status().isBadRequest());

        // Expecting no changes in the account balance
        checkAccountBalance(100, 10);
        checkTransitAccountBalance(0, 0);
        checkForNoTransactionRecorded();
    }

    // AUXILIARY METHODS

    private void setupPayload() throws JsonProcessingException {
        payload =
                "{\"transactionToken\":\"tx_2\",\"amount\":10,\"type\":\"authorization.clearing\",\"status\":\"COMPLETION\",\"userToken\":\"userToken\",\"cardToken\":\"1003\",\"precedingTransactionToken\":\"tx_1\",\"payload\":\"json"
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
        account.setUserId(requestPayload.getUserToken());
        account.setCardId(requestPayload.getCardToken());
        account.setAccountBalance(new BigDecimal("100.00"));
        account.setReservationBalance(new BigDecimal("10.00"));
    }

    private void setupExpectedAccount() {
        expectedAccount = new Account();
        expectedAccount.setAccountId(1L);
        expectedAccount.setUserId(account.getUserId());
        expectedAccount.setCardId(account.getCardId());
        expectedAccount.setAccountBalance(formatBigDecimal(100));
        expectedAccount.setReservationBalance(formatBigDecimal(0));
    }

    private void setupPrecedingTransaction() {
        precedingTransaction = new Transaction();
        precedingTransaction.setTransactionId(null);
        precedingTransaction.setTransactionToken(UUID.randomUUID().toString());
        precedingTransaction.setTransactionType(requestPayload.getType());
        precedingTransaction.setAccount(account);
        precedingTransaction.setAmount(formatBigDecimal(requestPayload.getAmount()));
        precedingTransaction.setTransactionStatus(requestPayload.getStatus());
        precedingTransaction.setPayload(payload);
        precedingTransaction.setPrecedingTransactionToken(null);
    }

    private void setupExpectedTransaction() {
        expectedTransaction = new Transaction();
        expectedTransaction.setTransactionId(2L);
        expectedTransaction.setTransactionToken(requestPayload.getTransactionToken());
        expectedTransaction.setTransactionType(requestPayload.getType());
        expectedTransaction.setAccount(expectedAccount);
        expectedTransaction.setAmount(formatBigDecimal(requestPayload.getAmount()));
        expectedTransaction.setTransactionStatus("PENDING");
        expectedTransaction.setPrecedingTransactionToken(
                precedingTransaction.getTransactionToken());
        expectedTransaction.setPayload(payload);
        expectedTransaction.setPrecedingTransactionToken(
                precedingTransaction.getTransactionToken());

        requestPayload.setPrecedingTransactionToken(precedingTransaction.getTransactionToken());
    }

    private void setRequestPayloadForReversal() {
        requestPayload.setType("authorization.reversal");
        requestPayload.setStatus("PENDING");
        expectedTransaction.setTransactionType("authorization.reversal");
        expectedTransaction.setTransactionStatus("PENDING");
    }

    private void setRequestPayloadForClearing() {
        requestPayload.setType("authorization.clearing");
        requestPayload.setStatus("COMPLETION");
        expectedTransaction.setTransactionType("authorization.clearing");
        expectedTransaction.setTransactionStatus("COMPLETION");
    }

    private void checkAccountClearingDoesNotExists() {
        Optional<Account> accountOptional = accountPort.getById((long) TRANSIT_ACCOUNT_ID);
        assertFalse(accountOptional.isPresent());
    }

    private void createPrecedingTransaction() throws DuplicateTransactionException {
        assertNotNull(
                account.getAccountId(),
                "You need to call the method 'saveAccountChangesIntoDB' before calling this"
                        + " method");
        precedingTransaction.getAccount().setAccountId(account.getAccountId());
        transactionPort.createTransaction(precedingTransaction);
    }

    private void performPostRequestAndExpect(
            String endpoint, String expectedResponsePayload, ResultMatcher statusMatcher)
            throws Exception {
        mockMvc.perform(
                        post(endpoint)
                                .contentType(CONTENT_TYPE)
                                .content(objectMapper.writeValueAsString(requestPayload)))
                .andExpect(statusMatcher)
                .andExpect(header().string("Content-Type", CONTENT_TYPE))
                .andExpect(content().string(expectedResponsePayload));
    }

    private void performPostRequestAndExpectForPartialText(
            String endpoint, String expectedPartialText) throws Exception {
        mockMvc.perform(
                        post(endpoint)
                                .contentType(CONTENT_TYPE)
                                .content(objectMapper.writeValueAsString(requestPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", CONTENT_TYPE))
                .andExpect(content().string(containsString(expectedPartialText)));
    }
}
