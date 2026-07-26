/* BlankFactor (C)2023 */
package com.accelerator.ledger.mapper;

import com.accelerator.ledger.dto.AccountingEntry;
import com.accelerator.ledger.dto.request.AccountingEntryRequest;

public interface AccountingEntryMapper {

    AccountingEntryRequest toRequest(AccountingEntry accountingEntry);
}
