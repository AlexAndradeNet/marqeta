/* BlankFactor (C)2023 */
package com.commons;

import com.blankfactor.Credentials;
import com.blankfactor.log.LoggerUtil;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class CommonRequests {

    private static final String BF_CORE_PUBLIC_URL = "BF_CORE_PUBLIC_URL";
    private static final String BF_MARQETA_PUBLIC_URL = "BF_MARQETA_PUBLIC_URL";

    @Step("POST {0} with payload variable {1}")
    public void post(String servicePath, String servicePayload) {
        post(servicePath, servicePayload, true);
    }

    @Step("POST {0}")
    public void post(String servicePath, String servicePayload, boolean isPayloadAVariable) {
        try {
            if (isPayloadAVariable) {
                servicePayload = EnvironmentProperties.getProperty(servicePayload);
            }

            servicePayload = replacePlaceholdersInPayload(servicePayload);
            servicePath = replacePlaceholdersInPath(EnvironmentProperties.getProperty(servicePath));

            String response = sendPostRequest(servicePath, servicePayload);
            logResponse(servicePath, servicePayload, response);
        } catch (Exception e) {
            LoggerUtil.error(CommonRequests.class, "POST request failed for " + servicePath, e);
        }
    }

    private String replacePlaceholdersInPath(String path) {
        return path.replace(BF_CORE_PUBLIC_URL, Credentials.getInstance().getBfBankPublicUrl())
                .replace(BF_MARQETA_PUBLIC_URL, Credentials.getInstance().getBfMarqetaPublicUrl());
    }

    private String replacePlaceholdersInPayload(String payload) {
        return payload.replace("\\\"", "\"")
                .replace(BF_CORE_PUBLIC_URL, Credentials.getInstance().getBfBankPublicUrl())
                .replace(BF_MARQETA_PUBLIC_URL, Credentials.getInstance().getBfMarqetaPublicUrl());
    }

    private String sendPostRequest(String servicePath, String servicePayload) {
        return SerenityRest.rest()
                .given()
                .spec(ReusableSpecifications.getGenericRequestSpec())
                .auth()
                .preemptive()
                .basic(
                        Credentials.getInstance().getUsername(),
                        Credentials.getInstance().getPassword())
                .when()
                .body(servicePayload)
                .post(servicePath)
                .then()
                .extract()
                .body()
                .asString();
    }

    private void logResponse(String servicePath, String servicePayload, String response) {
        LoggerUtil.info(CommonRequests.class, "POST " + servicePath + " " + servicePayload);
        LoggerUtil.info(CommonRequests.class, "RESPONSE " + response);
    }
}
