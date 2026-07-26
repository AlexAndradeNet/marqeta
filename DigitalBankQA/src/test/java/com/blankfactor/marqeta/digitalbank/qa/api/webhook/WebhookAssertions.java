/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa.api.webhook;

import com.commons.CommonAssertions;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class WebhookAssertions {
    @Steps private CommonAssertions commonAssertions;

    @Step("Verifying webhook response")
    public void verifyFullResponse() {
        commonAssertions.shouldSeeSuccessfulStatusCode();
    }
}
