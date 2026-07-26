/* BlankFactor (C)2023 */
package com.marqeta.authorization;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marqeta.authorization.dto.request.AuthorizationRequest;
import java.io.IOException;
import java.util.UUID;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Random.class)
public class BaseTest {
    @Autowired protected MockMvc mockMvc;
    @Autowired protected static ObjectMapper objectMapper;
    protected static final String CONTENT_TYPE = "application/json";
    protected static final int SERVER_PORT = 8089;
    protected static final String MARQUETA_RESPONSE_ERROR_TEMPLATE =
            "{\"status\":%s,\"message\":\"%s\"}";
    private MockWebServer mockWebServerNonStatic;
    private static String basePayload;

    @BeforeAll
    protected static void setUp() {
        objectMapper = new ObjectMapper();

        basePayload =
                "{\"type\":\"authorization\",\"state\":\"PENDING\",\"token\":\"de60d3f9-c294-4448-84da-d2d07e17de79\",\"gpa\":{\"balances\":{\"USD\":{\"currency_code\":\"USD\",\"ledger_balance\":0,\"available_balance\":0,\"credit_balance\":0,\"pending_credits\":0}},\"currency_code\":\"USD\",\"ledger_balance\":0,\"available_balance\":0,\"credit_balance\":0,\"pending_credits\":0.0},\"amount\":100,\"network\":\"DISCOVER\",\"acquirer\":{\"system_trace_audit_number\":\"458339\"},\"user\":{\"metadata\":{}},\"card\":{\"metadata\":{},\"last_four\":\"8097\"},\"pos\":{\"pin_present\":false,\"partial_approval_capable\":true,\"purchase_amount_only\":false,\"is_recurring\":false,\"is_installment\":false},\"preceding_related_transaction_token\":null,\"user_token\":\"ef41be75-6f05-4006-b49b-89365369a72e\",\"acting_user_token\":\"ef41be75-6f05-4006-b49b-89365369a72e\",\"card_token\":\"card_1\",\"card_product_token\":\"my_cardproduct_01\",\"original_credit\":{\"fast_funds_enabled\":false},\"gpa_order\":{\"token\":\"5f914870-adb4-4ce1-b38b-72bd317756a3\",\"amount\":100,\"state\":\"PENDING\",\"funding\":{\"amount\":100,\"source\":{\"type\":\"programgateway\",\"token\":\"**********a82e\",\"name\":\"JIT"
                    + " Funding Source"
                    + " carlos\",\"active\":true,\"is_default_account\":false,\"created_time\":\"2023-07-27T20:40:36Z\",\"last_modified_time\":\"2023-07-27T20:40:36Z\"}},\"memo\":null,\"tags\":null,\"transaction_token\":\"token1\",\"funding_source_token\":\"**********a82e\",\"jit_funding\":{\"token\":\"be68d8a8-0bb3-4f95-909c-8f3c377b75fd\",\"method\":\"pgfs.authorization\",\"amount\":100,\"user_token\":\"ef41be75-6f05-4006-b49b-89365369a72e\",\"acting_user_token\":\"ef41be75-6f05-4006-b49b-89365369a72e\"},\"user_token\":\"ef41be75-6f05-4006-b49b-89365369a72e\",\"currency_code\":\"USD\"},\"created_time\":\"2023-07-27T21:01:06Z\",\"user_transaction_time\":\"2023-07-27T21:01:06Z\",\"settlement_date\":\"2023-07-27T00:00:00Z\",\"request_amount\":100,\"currency_code\":\"USD\",\"acquirer_fee_amount\":0,\"issuer_received_time\":\"2023-07-27T21:01:06.304Z\",\"issuer_payment_node\":\"90d8f89dd75c0b6220f80a225b636f96\",\"network_reference_id\":\"517481074474\",\"card_acceptor\":{\"mid\":\"11111\",\"mcc\":\"6411\",\"name\":\"Chicken"
                    + " Tooth"
                    + " Music\",\"city\":\"Berkeley\",\"state\":\"CA\",\"street_address\":\"111"
                    + " Main St\",\"postal_code\":\"94702\",\"country_code\":\"USA\"}}";
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (mockWebServerNonStatic != null) {
            mockWebServerNonStatic.shutdown();
        }
    }

    @Test
    void emptyTest() {
        // This test is here to prevent JUnit from complaining about no tests
        // being present in this class
        assertTrue(true);
    }

    protected static String getCoreServiceResponse(String token, String message) {
        return String.format("{\"token\":\"%s\",\"message\":\"%s\"}", token, message);
    }

    protected static void verifyNetworkError(
            ResultActions resultActions, String marqetaServiceExpectedResponsePayload)
            throws Exception {
        resultActions
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(header().string("content-type", CONTENT_TYPE));
        try {
            resultActions.andExpect(
                    content()
                            .json(
                                    marqetaServiceExpectedResponsePayload.formatted(
                                            "Connection reset")));
        } catch (AssertionError e) {
            resultActions.andExpect(
                    content()
                            .json(
                                    marqetaServiceExpectedResponsePayload.formatted(
                                            "Unexpected end of file from server")));
        }
    }

    protected ResultActions performPostRequest(String endpoint, Object requestPayload)
            throws Exception {
        return mockMvc.perform(
                post(endpoint)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(requestPayload)));
    }

    protected void performPostRequestAndExpectPartialText(
            ResultActions resultActions, String text, HttpStatus responseStatus) throws Exception {
        resultActions
                .andExpect(status().is(responseStatus.value()))
                .andExpect(header().string("content-type", CONTENT_TYPE))
                .andExpect(content().string(containsString(text)));
    }

    protected void performPostRequestAndExpect(
            ResultActions resultActions, String expectedResponsePayload, HttpStatus responseStatus)
            throws Exception {
        resultActions
                .andExpect(status().is(responseStatus.value()))
                .andExpect(header().string("content-type", CONTENT_TYPE))
                .andExpect(content().json(expectedResponsePayload));
    }

    protected void performPostRequestAndExpectEmptyResponse(
            ResultActions resultActions, HttpStatus responseStatus) throws Exception {
        resultActions
                .andExpect(status().is(responseStatus.value()))
                .andExpect(header().doesNotExist("content-type"))
                .andExpect(content().string(""));
    }

    protected AuthorizationRequest getPayload() throws JsonProcessingException {
        AuthorizationRequest requestPayload =
                objectMapper.readValue(basePayload, AuthorizationRequest.class);

        requestPayload.setActingUserToken(UUID.randomUUID().toString());
        requestPayload.getGpaOrder().getJitFunding().setToken(UUID.randomUUID().toString());
        requestPayload.setUserToken(UUID.randomUUID().toString());
        requestPayload.setPrecedingTransactionToken(null);

        return requestPayload;
    }

    protected void startCoreMockServer() throws IOException {
        mockWebServerNonStatic = new MockWebServer();
        mockWebServerNonStatic.start(SERVER_PORT);
    }

    protected void setCoreMockResponse(String response, HttpStatus httpStatus) {
        mockWebServerNonStatic.enqueue(
                new MockResponse()
                        .setBody(response)
                        .setResponseCode(httpStatus.value())
                        .addHeader("Content-Type", CONTENT_TYPE));
    }

    protected void setCoreMockResponseToNetworkFailure() {
        mockWebServerNonStatic.enqueue(
                new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START));
    }
}
