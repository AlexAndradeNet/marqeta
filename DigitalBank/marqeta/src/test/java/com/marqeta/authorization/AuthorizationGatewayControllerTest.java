/* BlankFactor (C)2023 */
package com.marqeta.authorization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marqeta.authorization.dto.request.AuthorizationRequest;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

class AuthorizationGatewayControllerTest extends BaseTest {

    private static final String ENDPOINT = "/external/gateway/jit";
    private static AuthorizationRequest requestPayload = new AuthorizationRequest();
    private static final String MARQUETA_RESPONSE_SUCCESS =
            "{\"jit_funding\":{\"token\":\"%s\",\"method\":\"pgfs.authorization\",\"amount\":100,\"user_token\":\"%s\",\"acting_user_token\":\"%s\"}}";
    // TODO: We need to standardize the error response to the
    // BaseTest.MARQUETA_RESPONSE_ERROR_TEMPLATE
    private static final String MARQUETA_RESPONSE_ERROR_TWO =
            "{\"jit_funding\":{\"token\":\"%s\",\"method\":\"pgfs.authorization\",\"amount\":100,\"user_token\":\"%s\",\"acting_user_token\":\"%s\",\"decline_reason\":\"REASON_OF_FAILURE\"}}";
    private static final String MARQUETA_RESPONSE_ERROR_THREE = "";

    @BeforeEach
    public void init() throws IOException {
        requestPayload = getPayload();
        startCoreMockServer();
    }

    @Test
    void shouldAuthorizeSuccess() throws Exception {
        String marqetaServiceExpectedResponsePayload = getResponsePayloadForSuccessfully();
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        HttpStatus responseStatus = HttpStatus.OK;

        setCoreMockResponse(coreServiceResponsePayload, responseStatus);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, responseStatus);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldAuthorizeFailsForBadRequest() throws Exception {
        requestPayload.setType(null);
        HttpStatus marqetaStatus = HttpStatus.BAD_REQUEST;
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(
                        marqetaStatus.value(), "Validation error: type is required");
        String coreServiceResponsePayload = getCoreServiceResponse("OK");

        setCoreMockResponse(coreServiceResponsePayload, HttpStatus.OK);

        performPostRequestAndExpect(
                performPostRequest(), marqetaServiceExpectedResponsePayload, marqetaStatus);
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
                "OK"
            })
    void shouldAuthorizeFailsForControlledError(String reasonOfFailure) throws Exception {
        String marqetaServiceExpectedResponsePayload =
                getResponsePayloadForFailure(reasonOfFailure);
        String coreServiceResponsePayload = getCoreServiceResponse(reasonOfFailure);

        setCoreMockResponse(coreServiceResponsePayload, HttpStatus.BAD_REQUEST);

        performPostRequestAndExpect(
                performPostRequest(),
                marqetaServiceExpectedResponsePayload,
                HttpStatus.BAD_REQUEST);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldAuthorizeFailsDueToTheRequestIsNull() throws Exception {
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        requestPayload = null;

        setCoreMockResponse(coreServiceResponsePayload, HttpStatus.OK);

        // TODO: standardize this response
        performPostRequestAndExpectEmptyResponse(performPostRequest(), HttpStatus.BAD_REQUEST);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldAuthorizeFailsDueToTheRequestHasNullValues() throws Exception {
        String coreServiceResponsePayload = getCoreServiceResponse("OK");
        requestPayload = new AuthorizationRequest();

        setCoreMockResponse(coreServiceResponsePayload, HttpStatus.OK);

        performPostRequestAndExpectPartialText(
                performPostRequest(), "Validation error", HttpStatus.BAD_REQUEST);
        assertTrue(true); // This test is here to prevent JUnit from complaining about no tests
    }

    @Test
    void shouldAuthorizeFailsDueToCoreNetworkFailure() throws Exception {
        setCoreMockResponseToNetworkFailure();
        // TODO: Simplify the marqetaServiceExpectedResponsePayload
        String message = "%s executing POST http://localhost:8089/port/authorization-gateway";
        String marqetaServiceExpectedResponsePayload =
                MARQUETA_RESPONSE_ERROR_TEMPLATE.formatted(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(), message);

        verifyNetworkError(performPostRequest(), marqetaServiceExpectedResponsePayload);
    }

    // AUXILIARY METHODS

    private String getResponsePayloadForSuccessfully() {
        return formatResponsePayload(MARQUETA_RESPONSE_SUCCESS);
    }

    private String getResponsePayloadForFailure(String reasonOfFailure) {
        return formatResponsePayload(
                MARQUETA_RESPONSE_ERROR_TWO.replace("REASON_OF_FAILURE", reasonOfFailure));
    }

    private String formatResponsePayload(String responsePayload) {
        return String.format(
                responsePayload,
                requestPayload.getGpaOrder().getJitFunding().getToken(),
                requestPayload.getUserToken(),
                requestPayload.getActingUserToken());
    }

    private String getCoreServiceResponse(String message) {
        return getCoreServiceResponse(requestPayload.getToken(), message);
    }

    private ResultActions performPostRequest() throws Exception {
        return performPostRequest(ENDPOINT, requestPayload);
    }
}
