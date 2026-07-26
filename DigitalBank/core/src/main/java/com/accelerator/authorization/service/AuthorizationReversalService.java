/* BlankFactor (C)2023 */
package com.accelerator.authorization.service;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;

public interface AuthorizationReversalService {
    void reverseAuthorization(Authorization authorization)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    DuplicateTransactionException;
}
