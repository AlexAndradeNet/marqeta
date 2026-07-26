/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa.steps;

import com.blankfactor.dto.AuthorizationDTO;
import com.blankfactor.marqeta.digitalbank.qa.api.account.AccountRequests;
import com.marqeta.api.card.CardRequests;
import com.marqeta.api.fundingsources.ProgramGatewayRequests;
import com.marqeta.api.product.ProductRequests;
import com.marqeta.api.simulate.authorization.AuthorizationRequests;
import com.marqeta.api.user.UserRequests;
import net.thucydides.core.annotations.Steps;

public class CommonMethodsForClearingAndReverse {
    @Steps private ProgramGatewayRequests programGatewayRequests;
    @Steps private ProductRequests productRequests;
    @Steps private AccountRequests accountRequests;
    @Steps private UserRequests userRequests;
    @Steps private CardRequests cardRequests;
    @Steps private AuthorizationRequests authorizationRequests;
    protected final AuthorizationDTO authorizationDTO = new AuthorizationDTO();

    public String createTransactionAccountAndAuthorize() {
        authorizationDTO.setFundingToken(programGatewayRequests.createFundingSource());
        authorizationDTO.setProductToken(
                productRequests.create(authorizationDTO.getFundingToken()));

        authorizationDTO.setUserToken(userRequests.create());
        authorizationDTO.setCardToken(
                cardRequests.create(
                        authorizationDTO.getUserToken(), authorizationDTO.getProductToken()));

        accountRequests.create(authorizationDTO.getCardToken());

        authorizationDTO.setTransactionToken(
                authorizationRequests.authorizeAndVerifyTransactionState(
                        authorizationDTO.getCardToken()));

        return authorizationDTO.getTransactionToken();
    }
}
