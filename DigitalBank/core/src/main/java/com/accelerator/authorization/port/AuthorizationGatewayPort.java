/* BlankFactor (C)2023 */
package com.accelerator.authorization.port;

import com.accelerator.authorization.dto.request.AuthorizationRequest;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;
import com.accelerator.ledger.exception.LedgerAPIException;

public interface AuthorizationGatewayPort {

    void processAuthorization(AuthorizationRequest authorization)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    LedgerAPIException,
                    DuplicateTransactionException;
}
