/* BlankFactor (C)2023 */
package com.accelerator.ledger.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AccountingEntry {
    private String transactionType;
    private BigDecimal amount;
    private String currencyAlphaCode;
    private String accountingEntryTimestamp;
    private String accountingEntryDetails;
    private String schemeBrand;
    private String merchantName;
    private String merchantCity;
    private String merchantCountry;
    private Account account;

    @Builder
    @Data
    @AllArgsConstructor
    public static class Account {
        private String accountId;
    }
}
