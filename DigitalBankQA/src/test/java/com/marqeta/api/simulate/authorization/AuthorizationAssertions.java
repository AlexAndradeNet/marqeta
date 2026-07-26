/* BlankFactor (C)2023 */
package com.marqeta.api.simulate.authorization;

import com.blankfactor.enums.TransactionStateEnum;
import com.commons.CommonAssertions;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class AuthorizationAssertions {
    @Steps private CommonAssertions commonAssertions;

    private static final String RESPONSE_SCHEMA = "marqeta.simulate.authorization.schema";

    @Step("Verifying authorization state")
    public void verifyState(TransactionStateEnum transactionStateEnum) {
        commonAssertions.validateFieldValue(
                AuthorizationResponse.STATE, transactionStateEnum.toString());
    }

    @Step("Verifying authorization response")
    public String verifyFullCreatedResponseAndSchema() {
        commonAssertions.verifyCreatedResponseAndSchema(RESPONSE_SCHEMA);
        return commonAssertions.validateTokenIsGuidAndReturnIt(AuthorizationResponse.TOKEN);
    }
}
