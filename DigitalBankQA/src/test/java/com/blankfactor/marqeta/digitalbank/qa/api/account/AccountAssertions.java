/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa.api.account;

import com.commons.CommonAssertions;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class AccountAssertions {
    @Steps private CommonAssertions commonAssertions;

    private static final String RESPONSE_SCHEMA = "blankfactor.account.schema";

    @Step("Verifying account response")
    public void verifyFullCreatedResponseAndSchema() {
        commonAssertions.verifyCreatedResponseAndSchema(RESPONSE_SCHEMA);
    }
}
