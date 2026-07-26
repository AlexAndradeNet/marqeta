/* BlankFactor (C)2023 */
package com.marqeta.api.simulate.clearing;

import com.blankfactor.enums.TransactionStateEnum;
import com.blankfactor.enums.TransactionTypeEnum;
import com.commons.CommonAssertions;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class ClearingAssertions {
    @Steps private CommonAssertions commonAssertions;

    private static final String RESPONSE_SCHEMA = "marqeta.simulate.clearing.schema";

    @Step("Verifying clearing state")
    public void verifyState(TransactionStateEnum transactionStateEnum) {
        commonAssertions.validateFieldValue(
                ClearingResponse.STATE, transactionStateEnum.toString());
    }

    @Step("Verifying clearing response")
    public String verifyFullCreatedResponseAndSchema() {
        commonAssertions.verifyCreatedResponseAndSchema(RESPONSE_SCHEMA);
        commonAssertions.validateFieldValue(
                ClearingResponse.TYPE, TransactionTypeEnum.CLEARING.getValue());
        return commonAssertions.validateTokenIsGuidAndReturnIt(ClearingResponse.TOKEN);
    }
}
