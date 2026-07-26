/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa.api.account;

import com.commons.CommonRequests;
import com.commons.EnvironmentProperties;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class AccountRequests {

    private static final String SERVICE_PATH = "blankfactor.account.endpoint";
    private static final String SERVICE_PAYLOAD = "blankfactor.account.payload";
    @Steps private CommonRequests commonRequests;
    @Steps private AccountAssertions accountAssertions;

    @Step("Create an account in Blankfactor's Bank")
    public void create(String cardToken) {
        String customPayload = getCustomPayload(cardToken);

        commonRequests.post(SERVICE_PATH, customPayload, false);
        accountAssertions.verifyFullCreatedResponseAndSchema();
    }

    private static String getCustomPayload(String cardToken) {
        String payload = EnvironmentProperties.getProperty(SERVICE_PAYLOAD);
        payload = payload.replace("CARD_TOKEN", cardToken);
        return payload;
    }
}
