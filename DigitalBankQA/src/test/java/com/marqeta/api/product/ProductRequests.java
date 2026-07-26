/* BlankFactor (C)2023 */
package com.marqeta.api.product;

import com.commons.CommonAssertions;
import com.commons.CommonRequests;
import com.commons.EnvironmentProperties;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class ProductRequests {

    private static final String SERVICE_PATH = "marqeta.product.endpoint";
    private static final String SERVICE_PAYLOAD = "marqeta.product.payload";
    private static final String RESPONSE_SCHEMA = "marqeta.product.schema";
    private final String PRODUCT_TOKEN = "AutomationProduct" + System.currentTimeMillis();
    @Steps private CommonRequests commonRequests;
    @Steps private CommonAssertions commonAssertions;

    @Step("Create a product")
    public String create(String fundingToken) {
        String customPayload = getCustomPayload(fundingToken);

        commonRequests.post(SERVICE_PATH, customPayload, false);
        commonAssertions.verifyCreatedResponseAndSchema(RESPONSE_SCHEMA);
        commonAssertions.validateFieldValue(ProductResponse.TOKEN, PRODUCT_TOKEN);
        return PRODUCT_TOKEN;
    }

    private String getCustomPayload(String fundingToken) {
        String payload = EnvironmentProperties.getProperty(ProductRequests.SERVICE_PAYLOAD);
        payload = payload.replace("PRODUCT_TOKEN", PRODUCT_TOKEN);
        payload = payload.replace("FUNDING_TOKEN", fundingToken);
        return payload;
    }
}
