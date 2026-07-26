/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa.steps;

import com.blankfactor.enums.TransactionStateEnum;
import com.blankfactor.marqeta.digitalbank.qa.api.account.AccountRequests;
import com.marqeta.api.card.CardRequests;
import com.marqeta.api.fundingsources.ProgramGatewayRequests;
import com.marqeta.api.product.ProductRequests;
import com.marqeta.api.simulate.authorization.AuthorizationAssertions;
import com.marqeta.api.simulate.authorization.AuthorizationRequests;
import com.marqeta.api.user.UserRequests;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

public class AuthorizationStepDefinitions {

    @Steps private ProgramGatewayRequests programGatewayRequests;
    @Steps private ProductRequests productRequests;
    @Steps private AccountRequests accountRequests;
    @Steps private AuthorizationRequests authorizationRequests;
    @Steps private UserRequests userRequests;
    @Steps private CardRequests cardRequests;
    @Steps private AuthorizationAssertions authorizationAssertions;
    private static String cardToken;

    @Given("I am a user with a valid credit card")
    public void createCreditCard() {
        String fundingToken = programGatewayRequests.createFundingSource();
        String productToken = productRequests.create(fundingToken);
        String userToken = userRequests.create();
        cardToken = cardRequests.create(userToken, productToken);
        accountRequests.create(cardToken);
    }

    @When("I simulate an authorization")
    public void authorizeTransaction() {
        String transactionToken = authorizationRequests.authorize(cardToken);
    }

    @Then("the response of transaction is {transactionStateEnum}")
    public void getAResponseOfTransactionAs(TransactionStateEnum transactionStateEnum) {
        authorizationAssertions.verifyState(transactionStateEnum);
    }
}
