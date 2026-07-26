/* BlankFactor (C)2023 */
package com.accelerator.authorization.service;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.common.dto.Account;
import com.accelerator.common.exception.TransitAccountException;
import com.accelerator.ledger.exception.LedgerAPIException;

public interface AuthorizationLedgerService {

    String processReservation(Authorization authorization, Account account)
            throws LedgerAPIException, TransitAccountException;
}
