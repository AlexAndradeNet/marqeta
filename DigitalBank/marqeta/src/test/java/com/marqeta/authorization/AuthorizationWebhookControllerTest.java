/* BlankFactor (C)2023 */
package com.marqeta.authorization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.marqeta.authorization.dto.request.AuthorizationNotificationRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

class AuthorizationWebhookControllerTest extends BaseTest {

    private static final String ENDPOINT = "/external/webhook";
    private static AuthorizationNotificationRequest requestPayload =
            new AuthorizationNotificationRequest();

    private static final String MARQUETA_RESPONSE_SUCCESS = "";

    @BeforeEach
    public void init() throws IOException {
        setupBasePayload();
        startCoreMockServer();
    }

    @Test
    void shouldClearingSuccessfullyWhenValidRequest() throws Exception {
        setupPayloadForClearing();
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        HttpStatus responseStatus = HttpStatus.OK;

        setCoreMockResponse(coreServiceResponsePayload, responseStatus);

        performPostRequestAndExpectEmptyResponse(performPostRequest(), responseStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldReversalSuccessfullyWhenValidRequest() throws Exception {
        setupPayloadForReversal();
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        HttpStatus responseStatus = HttpStatus.OK;

        setCoreMockResponse(coreServiceResponsePayload, responseStatus);

        performPostRequestAndExpectEmptyResponse(performPostRequest(), responseStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "INVALID_CARD",
                "INSUFFICIENT_FUNDS",
                "INVALID_TRANSACTION_TYPE",
                "TRANSACTION_NOT_PERMITTED",
                "INVALID_TRANSACTION_STATUS",
                "INVALID_PRECEDING_TRANSACTION_TOKEN",
                "OK"
            })
    void shouldClearingFailsForControlledError(String reasonOfFailure) throws Exception {
        setupPayloadForClearing();
        String coreServiceResponsePayload = getCoreServiceResponse(reasonOfFailure);
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message =
                "[500 Server Error] during [POST] to"
                        + " [http://localhost:8089/port/authorization-webhook/clearing]"
                        + " [CoreAuthWebhookClient#processClearing(CoreAuthorizationRequest)]:"
                        + " [{\\\"token\\\":\\\"%s\\\",\\\"message\\\":\\\"%s\\\"}]"
                                .formatted(
                                        requestPayload.getTransactions().get(0).getToken(),
                                        reasonOfFailure);
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(responseStatus.value(), message);

        setCoreMockResponse(coreServiceResponsePayload, responseStatus);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, responseStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "INVALID_CARD",
                "INSUFFICIENT_FUNDS",
                "INVALID_TRANSACTION_TYPE",
                "TRANSACTION_NOT_PERMITTED",
                "INVALID_TRANSACTION_STATUS",
                "INVALID_PRECEDING_TRANSACTION_TOKEN",
                "OK"
            })
    void shouldReversalFailsForControlledError(String reasonOfFailure) throws Exception {
        setupPayloadForReversal();
        String coreServiceResponsePayload = getCoreServiceResponse(reasonOfFailure);
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message =
                "[500 Server Error] during [POST] to"
                        + " [http://localhost:8089/port/authorization-webhook/reversal]"
                        + " [CoreAuthWebhookClient#processReversal(CoreAuthorizationRequest)]:"
                        + " [{\\\"token\\\":\\\"%s\\\",\\\"message\\\":\\\"%s\\\"}]"
                                .formatted(
                                        requestPayload.getTransactions().get(0).getToken(),
                                        reasonOfFailure);
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(responseStatus.value(), message);

        setCoreMockResponse(coreServiceResponsePayload, responseStatus);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, responseStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldClearingFailsForTransitAccountNotFound() throws Exception {
        setupPayloadForClearing();
        String coreServiceResponsePayload = "{\"message\":\"Transit account not found\"}";
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        // TODO: Simplify the marqetaServiceExpectedResponsePayload
        String message =
                "[500 Server Error] during [POST] to"
                        + " [http://localhost:8089/port/authorization-webhook/clearing]"
                        + " [CoreAuthWebhookClient#processClearing(CoreAuthorizationRequest)]:"
                        + " [{\\\"message\\\":\\\"Transit account not found\\\"}]";
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(responseStatus.value(), message);

        setCoreMockResponse(coreServiceResponsePayload, responseStatus);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, responseStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldClearingFailsDueToTheRequestIsNull() throws Exception {
        setupPayloadForClearing();
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        requestPayload = null;

        setCoreMockResponse(coreServiceResponsePayload, HttpStatus.OK);

        performPostRequestAndExpectEmptyResponse(performPostRequest(), HttpStatus.BAD_REQUEST);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldClearingFailsDueToTransactionsListIsNull() throws Exception {
        setupPayloadForClearing();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = "Validation error: transactions list is required";
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(httpStatus.value(), message);
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        requestPayload.setTransactions(null);

        setCoreMockResponse(coreServiceResponsePayload, HttpStatus.OK);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, httpStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldFailsDueToTransactionsListIsEmpty() throws Exception {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = "Validation error: transactions list is required";
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(httpStatus.value(), message);
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        requestPayload.setTransactions(new ArrayList<>());

        setCoreMockResponse(coreServiceResponsePayload, HttpStatus.OK);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, httpStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldFailsDueToTransactionsListHasNullValues() throws Exception {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = "Validation error: transactions list is required";
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(httpStatus.value(), message);
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        requestPayload.setTransactions(new ArrayList<>());
        requestPayload.getTransactions().add(null);

        setCoreMockResponse(coreServiceResponsePayload, HttpStatus.OK);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, httpStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldFailsDueAWrongType() throws Exception {
        setupPayloadForClearing();
        requestPayload.getTransactions().get(0).setType("wrong.type");
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        HttpStatus httpStatus = HttpStatus.OK;

        setCoreMockResponse(coreServiceResponsePayload, httpStatus);

        performPostRequestAndExpectEmptyResponse(performPostRequest(), httpStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldReversalFailsDueToCoreNetworkFailure() throws Exception {
        setupPayloadForReversal();
        setCoreMockResponseToNetworkFailure();
        String message =
                "%s executing POST http://localhost:8089/port/authorization-webhook/reversal";
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(), message);

        verifyNetworkError(performPostRequest(), marqetaServiceExpectedResponsePayload);
    }

    @Test
    void shouldClearingFailsDueToCoreNetworkFailure() throws Exception {
        setupPayloadForClearing();
        setCoreMockResponseToNetworkFailure();
        String message =
                "%s executing POST http://localhost:8089/port/authorization-webhook/clearing";
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(), message);

        verifyNetworkError(performPostRequest(), marqetaServiceExpectedResponsePayload);
    }

    @Test
    void shouldClearingFailsDueToPrecedingTokenIsNull() throws Exception {
        setupPayloadForClearing();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        requestPayload.getTransactions().get(0).setPrecedingTransactionToken(null);
        setCoreMockResponseToNetworkFailure();
        String message = "Validation error: preceding_transaction_token is required";
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(httpStatus.value(), message);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, httpStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldReversalFailsDueToPrecedingTokenIsNull() throws Exception {
        setupPayloadForReversal();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        requestPayload.getTransactions().get(0).setPrecedingTransactionToken(null);
        setCoreMockResponseToNetworkFailure();
        String message = "Validation error: preceding_transaction_token is required";
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(httpStatus.value(), message);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, httpStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    // AUXILIARY METHODS

    private void setupBasePayload() throws JsonProcessingException {
        requestPayload = new AuthorizationNotificationRequest();
        requestPayload.setTransactions(new ArrayList<>());
        requestPayload.getTransactions().add(getPayload());
        requestPayload
                .getTransactions()
                .get(0)
                .setCardAcceptor(null); // This is not necessary for this endpoint
        requestPayload
                .getTransactions()
                .get(0)
                .setPrecedingTransactionToken(UUID.randomUUID().toString());
    }

    private void setupPayloadForClearing() {
        requestPayload.getTransactions().get(0).setType("authorization.clearing");
        requestPayload.getTransactions().get(0).setState("COMPLETION");
    }

    private void setupPayloadForReversal() {
        requestPayload.getTransactions().get(0).setType("authorization.reversal");
        requestPayload.getTransactions().get(0).setState("PENDING");
    }

    private ResultActions performPostRequest() throws Exception {
        return performPostRequest(ENDPOINT, requestPayload);
    }

    private String getCoreServiceResponse(String message) {
        return getCoreServiceResponse(requestPayload.getTransactions().get(0).getToken(), message);
    }
}
