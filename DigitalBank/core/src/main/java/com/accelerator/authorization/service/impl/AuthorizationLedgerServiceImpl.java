/* BlankFactor (C)2023 */
package com.accelerator.authorization.service.impl;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.service.AuthorizationLedgerService;
import com.accelerator.common.dto.Account;
import com.accelerator.common.exception.TransitAccountException;
import com.accelerator.common.port.AccountPort;
import com.accelerator.ledger.dto.AccountingEntry;
import com.accelerator.ledger.exception.LedgerAPIException;
import com.accelerator.ledger.port.LedgerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationLedgerServiceImpl implements AuthorizationLedgerService {

    private static final String TRANSACTION_TYPE = "DEBIT";
    private static final String AUTHORIZATION_RESERVATION = "AUTHORIZATION_RESERVATION";

    private final LedgerPort ledgerPort;
    private final AccountPort accountPort;

    @Value("${db.account.transit-account-id}")
    private Long transitAccountId;

    @Override
    public String processReservation(Authorization authorization, Account account)
            throws LedgerAPIException, TransitAccountException {

        Account transitAccount =
                accountPort
                        .getById(transitAccountId)
                        .orElseThrow(
                                () -> new TransitAccountException("Transit account not found"));

        AccountingEntry source =
                buildAccountingEntry(account.getAccountId().toString(), authorization);
        AccountingEntry target =
                buildAccountingEntry(transitAccount.getAccountId().toString(), authorization);

        return ledgerPort.processReservation(source, target);
    }

    private AccountingEntry buildAccountingEntry(String accountId, Authorization authorization) {
        return AccountingEntry.builder()
                .transactionType(TRANSACTION_TYPE)
                .amount(authorization.getAmount())
                .currencyAlphaCode(authorization.getCurrency())
                .accountingEntryTimestamp(authorization.getTimestamp())
                .accountingEntryDetails(AUTHORIZATION_RESERVATION)
                .schemeBrand(authorization.getScheme())
                .merchantName(authorization.getMerchantName())
                .merchantCity(authorization.getMerchantCity())
                .merchantCountry(authorization.getMerchantCountry())
                .account(AccountingEntry.Account.builder().accountId(accountId).build())
                .build();
    }
}
