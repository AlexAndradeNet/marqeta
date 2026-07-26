/* BlankFactor (C)2023 */
package com.marqeta.api.fundingsources;

import com.commons.CommonAssertions;
import com.commons.CommonRequests;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

public class ProgramGatewayRequests {

    private static final String SERVICE_PATH = "marqeta.funding.endpoint";
    private static final String SERVICE_PAYLOAD = "marqeta.funding.payload";
    private static final String RESPONSE_SCHEMA = "marqeta.funding.schema";
    @Steps private CommonRequests commonRequests;
    @Steps private CommonAssertions commonAssertions;

    @Step("Create a funding source")
    public String createFundingSource() {
        commonRequests.post(SERVICE_PATH, SERVICE_PAYLOAD);
        commonAssertions.verifyCreatedResponseAndSchema(RESPONSE_SCHEMA);
        return commonAssertions.validateTokenIsGuidAndReturnIt(ProgramGatewayResponse.TOKEN);
    }
}
