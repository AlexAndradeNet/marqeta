/* BlankFactor (C)2023 */
package com.marqeta.api.card;

import com.commons.CommonAssertions;
import com.commons.CommonRequests;
import com.commons.EnvironmentProperties;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class CardRequests {

    private static final String SERVICE_PATH = "marqeta.card.endpoint";
    private static final String SERVICE_PAYLOAD = "marqeta.card.payload";
    private static final String RESPONSE_SCHEMA = "marqeta.card.schema";
    @Steps private CommonRequests commonRequests;
    @Steps private CommonAssertions commonAssertions;

    @Step("Create a card")
    public String create(String userToken, String productToken) {
        String customPayload = getCustomPayload(userToken, productToken);

        commonRequests.post(SERVICE_PATH, customPayload, false);
        commonAssertions.verifyCreatedResponseAndSchema(RESPONSE_SCHEMA);
        return commonAssertions.validateTokenIsGuidAndReturnIt(CardResponse.TOKEN);
    }

    private static String getCustomPayload(String userToken, String productToken) {
        String payload = EnvironmentProperties.getProperty(CardRequests.SERVICE_PAYLOAD);
        payload = payload.replace("USER_TOKEN", userToken);
        payload = payload.replace("PRODUCT_TOKEN", productToken);
        return payload;
    }
}
