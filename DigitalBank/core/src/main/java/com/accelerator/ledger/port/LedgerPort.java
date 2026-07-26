/* BlankFactor (C)2023 */
package com.accelerator.ledger.port;

import com.accelerator.ledger.dto.AccountingEntry;
import com.accelerator.ledger.exception.LedgerAPIException;

public interface LedgerPort {

    String processReservation(AccountingEntry source, AccountingEntry target)
            throws LedgerAPIException;
}
