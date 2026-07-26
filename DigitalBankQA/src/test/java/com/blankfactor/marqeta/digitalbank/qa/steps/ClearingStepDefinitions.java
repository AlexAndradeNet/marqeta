/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa.steps;

import com.blankfactor.enums.TransactionStateEnum;
import com.blankfactor.enums.TransactionTypeEnum;
import com.blankfactor.marqeta.digitalbank.qa.api.webhook.WebhookAssertions;
import com.blankfactor.marqeta.digitalbank.qa.api.webhook.WebhookRequests;
import com.commons.EnvironmentProperties;
import com.marqeta.api.simulate.clearing.ClearingAssertions;
import com.marqeta.api.simulate.clearing.ClearingRequests;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

public class ClearingStepDefinitions extends CommonMethodsForClearingAndReverse {
    @Steps private ClearingRequests clearingRequests;
    @Steps private ClearingAssertions clearingAssertions;
    @Steps private WebhookRequests webhookRequests;
    @Steps private WebhookAssertions webhookAssertions;
    private String transactionToken;

    @Given("I am a user with an authorized transaction to clear")
    public void givenIAmAUserWithAnAuthorizedTransaction() {
        transactionToken = createTransactionAccountAndAuthorize();
    }

    @When("I clear the transaction")
    public void whenIClearTheTransaction() {
        clearingRequests.clear(transactionToken);
    }

    @Then("the response of clearing is {transactionStateEnum}")
    public void thenTheResponseOfClearingIs(TransactionStateEnum transactionStateEnum) {
        clearingAssertions.verifyState(transactionStateEnum);
    }

    @Given("I am a user with a transaction that has been cleared")
    public void givenIAmAUserWithATransactionThatHasBeenCleared() {
        givenIAmAUserWithAnAuthorizedTransaction();
        if (EnvironmentProperties.getProperty("marqeta.simulate.clearing.isInRealTime")
                .equals("false")) {
            whenIClearTheTransaction();
            thenTheResponseOfClearingIs(TransactionStateEnum.COMPLETION);
        }
    }

    @When("I mimic clearing the transaction")
    public void iMimicClearingTheTransaction() {
        webhookRequests.execute(TransactionTypeEnum.CLEARING, authorizationDTO);
    }

    @Then("the clearing response from BF bank is successful")
    public void theResponseFromBFBankIsSuccessful() {
        webhookAssertions.verifyFullResponse();
    }
}
