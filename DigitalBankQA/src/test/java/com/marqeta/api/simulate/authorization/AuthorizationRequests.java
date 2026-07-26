/* BlankFactor (C)2023 */
package com.marqeta.api.simulate.authorization;

import com.blankfactor.enums.TransactionStateEnum;
import com.commons.CommonRequests;
import com.commons.EnvironmentProperties;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class AuthorizationRequests {

    private static final String SERVICE_PATH = "marqeta.simulate.authorization.endpoint";
    private static final String SERVICE_PAYLOAD = "marqeta.simulate.authorization.payload";
    @Steps private CommonRequests commonRequests;
    @Steps private AuthorizationAssertions authorizationAssertions;

    public String authorize(String cardToken) {
        return authorize(cardToken, 1000);
    }

    @Step("Authorize a transaction")
    public String authorize(String cardToken, int amount) {
        String customPayload = getCustomPayload(cardToken, amount);

        commonRequests.post(SERVICE_PATH, customPayload, false);
        return authorizationAssertions.verifyFullCreatedResponseAndSchema();
    }

    private String getCustomPayload(String cardToken, int amount) {
        String payload = EnvironmentProperties.getProperty(AuthorizationRequests.SERVICE_PAYLOAD);
        payload = payload.replace("AMOUNT_VALUE", String.valueOf(amount));
        payload = payload.replace("CARD_TOKEN", cardToken);
        return payload;
    }

    public String authorizeAndVerifyTransactionState(String cardToken) {
        String transactionToken = authorize(cardToken);
        authorizationAssertions.verifyState(TransactionStateEnum.PENDING);
        return transactionToken;
    }
}
