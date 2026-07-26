/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa.api.webhook;

import com.blankfactor.dto.AuthorizationDTO;
import com.blankfactor.enums.TransactionTypeEnum;
import com.commons.CommonRequests;
import com.commons.EnvironmentProperties;
import java.util.UUID;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class WebhookRequests {

    private static final String SERVICE_PATH = "blankfactor.digitalbank.webhook.endpoint";
    private static final String SERVICE_PAYLOAD = "blankfactor.digitalbank.webhook.payload";
    @Steps private CommonRequests commonRequests;
    @Steps private WebhookAssertions webhookAssertions;

    @Step("Mimic an {2} in the BF bank")
    public void execute(
            TransactionTypeEnum transactionTypeEnum, AuthorizationDTO authorizationDTO) {
        String customPayload = getCustomPayload(transactionTypeEnum, authorizationDTO);

        commonRequests.post(SERVICE_PATH, customPayload, false);
        webhookAssertions.verifyFullResponse();
    }

    private static String getCustomPayload(
            TransactionTypeEnum transactionTypeEnum, AuthorizationDTO authorizationDTO) {
        String payload = EnvironmentProperties.getProperty(SERVICE_PAYLOAD);
        payload = payload.replace("TRANSACTION_TYPE", transactionTypeEnum.getValue());
        payload = payload.replace("GUID_RANDOM", UUID.randomUUID().toString());
        payload = payload.replace("AMOUNT_VALUE", "1000.00");
        payload = payload.replace("USER_TOKEN", authorizationDTO.getUserToken());
        payload = payload.replace("CARD_TOKEN", authorizationDTO.getCardToken());
        payload = payload.replace("PRODUCT_TOKEN", authorizationDTO.getProductToken());
        payload = payload.replace("TRANSACTION_TOKEN", authorizationDTO.getTransactionToken());

        return payload;
    }
}
