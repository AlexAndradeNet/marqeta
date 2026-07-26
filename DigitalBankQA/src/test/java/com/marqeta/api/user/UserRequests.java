/* BlankFactor (C)2023 */
package com.marqeta.api.user;

import com.commons.CommonAssertions;
import com.commons.CommonRequests;
import com.commons.EnvironmentProperties;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class UserRequests {

    private static final String SERVICE_PATH = "marqeta.user.endpoint";
    private static final String SERVICE_PAYLOAD = "marqeta.user.payload";
    private static final String RESPONSE_SCHEMA = "marqeta.user.schema";
    private static final String USER_TOKEN = "AutomationUser" + System.currentTimeMillis();
    private static final String EMAIL_RANDOM = System.currentTimeMillis() + "@blankfactor.com";
    @Steps private CommonRequests commonRequests;
    @Steps private CommonAssertions commonAssertions;

    @Step("Create a user")
    public String create() {
        String customPayload = getCustomPayload();

        commonRequests.post(SERVICE_PATH, customPayload, false);
        commonAssertions.verifyCreatedResponseAndSchema(RESPONSE_SCHEMA);
        commonAssertions.validateFieldValue(UserResponse.TOKEN, USER_TOKEN);
        return USER_TOKEN;
    }

    private static String getCustomPayload() {
        String payload = EnvironmentProperties.getProperty(SERVICE_PAYLOAD);
        payload = payload.replace("USER_TOKEN", USER_TOKEN);
        payload = payload.replace("EMAIL_RANDOM", EMAIL_RANDOM);
        return payload;
    }
}
