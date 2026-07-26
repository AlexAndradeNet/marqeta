/* BlankFactor (C)2023 */
package com.marqeta.api.simulate.reversal;

import com.commons.CommonRequests;
import com.commons.EnvironmentProperties;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class ReversalRequests {

    private static final String SERVICE_PATH = "marqeta.simulate.reversal.endpoint";
    private static final String SERVICE_PAYLOAD = "marqeta.simulate.reversal.payload";
    private static final String RESPONSE_SCHEMA = "marqeta.simulate.reversal.schema";
    @Steps private CommonRequests commonRequests;
    @Steps private ReversalAssertions reversalAssertions;

    public String reverse(String transactionToken) {
        return reverse(transactionToken, 1000);
    }

    @Step("Reversing previous transaction")
    public String reverse(String transactionToken, int amount) {
        String customPayload = getCustomPayload(transactionToken, amount);

        commonRequests.post(SERVICE_PATH, customPayload, false);
        return reversalAssertions.verifyFullCreatedResponseAndSchema();
    }

    private String getCustomPayload(String transactionToken, int amount) {
        String payload = EnvironmentProperties.getProperty(SERVICE_PAYLOAD);
        payload = payload.replace("AMOUNT_VALUE", String.valueOf(amount));
        payload = payload.replace("TRANSACTION_TOKEN", transactionToken);
        return payload;
    }
}
