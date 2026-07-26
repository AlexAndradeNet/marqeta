/* BlankFactor (C)2023 */
package com.marqeta.api.simulate.clearing;

import com.commons.CommonRequests;
import com.commons.EnvironmentProperties;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class ClearingRequests {

    private static final String SERVICE_PATH = "marqeta.simulate.clearing.endpoint";
    private static final String SERVICE_PAYLOAD = "marqeta.simulate.clearing.payload";
    @Steps private CommonRequests commonRequests;
    @Steps private ClearingAssertions clearingAssertions;

    public String clear(String transactionToken) {
        return clear(transactionToken, 1000);
    }

    @Step("Clearing previous transaction")
    public String clear(String transactionToken, int amount) {
        String customPayload = getCustomPayload(transactionToken, amount);

        commonRequests.post(SERVICE_PATH, customPayload, false);
        return clearingAssertions.verifyFullCreatedResponseAndSchema();
    }

    private String getCustomPayload(String transactionToken, int amount) {
        String payload = EnvironmentProperties.getProperty(SERVICE_PAYLOAD);
        payload = payload.replace("AMOUNT_VALUE", String.valueOf(amount));
        payload = payload.replace("TRANSACTION_TOKEN", transactionToken);
        return payload;
    }
}
