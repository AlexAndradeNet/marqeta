/* BlankFactor (C)2023 */
package com.marqeta.api.simulate.reversal;

import com.blankfactor.enums.TransactionStateEnum;
import com.blankfactor.enums.TransactionTypeEnum;
import com.commons.CommonAssertions;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class ReversalAssertions {
    @Steps private CommonAssertions commonAssertions;

    private static final String RESPONSE_SCHEMA = "marqeta.simulate.reversal.schema";

    @Step("Verifying reversal state")
    public void verifyState(TransactionStateEnum transactionStateEnum) {
        commonAssertions.validateFieldValue(
                ReversalResponse.STATE, transactionStateEnum.toString());
    }

    @Step("Verifying reversal response")
    public String verifyFullCreatedResponseAndSchema() {
        commonAssertions.verifyCreatedResponseAndSchema(RESPONSE_SCHEMA);
        commonAssertions.validateFieldValue(
                ReversalResponse.TYPE, TransactionTypeEnum.REVERSAL.getValue());
        return commonAssertions.validateTokenIsGuidAndReturnIt(ReversalResponse.TOKEN);
    }
}
