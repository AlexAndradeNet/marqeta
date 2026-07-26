/* BlankFactor (C)2023 */
package com.accelerator.authorization.service;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;
import com.accelerator.ledger.exception.LedgerAPIException;

public interface AuthorizationProcessingService {

    void processAuthorization(Authorization authorization)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    LedgerAPIException,
                    DuplicateTransactionException;
}
