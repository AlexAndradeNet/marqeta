/* BlankFactor (C)2023 */
package com.accelerator.ledger.mapper.impl;

import com.accelerator.ledger.dto.AccountingEntry;
import com.accelerator.ledger.dto.request.AccountingEntryRequest;
import com.accelerator.ledger.mapper.AccountingEntryMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountingEntryMapperImpl implements AccountingEntryMapper {

    @Override
    public AccountingEntryRequest toRequest(AccountingEntry accountingEntry) {
        return AccountingEntryRequest.builder()
                .transactionType(accountingEntry.getTransactionType())
                .amount(accountingEntry.getAmount().toString())
                .currencyAlphaCode(accountingEntry.getCurrencyAlphaCode())
                .accountingEntryTimestamp(accountingEntry.getAccountingEntryDetails())
                .accountingEntryDetails(accountingEntry.getAccountingEntryDetails())
                .schemeBrand(accountingEntry.getSchemeBrand())
                .merchantName(accountingEntry.getMerchantName())
                .merchantCity(accountingEntry.getMerchantCity())
                .merchantCountry(accountingEntry.getMerchantCountry())
                .account(
                        AccountingEntryRequest.Account.builder()
                                .accountId(accountingEntry.getAccount().getAccountId())
                                .build())
                .build();
    }
}
