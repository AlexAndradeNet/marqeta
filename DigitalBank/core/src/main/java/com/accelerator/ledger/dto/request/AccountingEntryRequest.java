/* BlankFactor (C)2023 */
package com.accelerator.ledger.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AccountingEntryRequest {
    private Integer version;
    private String transactionType;
    private String amount;
    private String currencyAlphaCode;
    private String accountingEntryTimestamp;
    private String accountingEntryDetails;
    private String schemeBrand;
    private String merchantName;
    private String merchantCity;
    private String merchantCountry;
    private String cardHolderFirstName;
    private String cardHolderLastName;
    private String maskedPan;
    private Integer originalAmount;
    private String originalCurrencyCode;
    private String fxRate;
    private String gpsLatitude;
    private String gpsLongitude;
    private Account account;

    @Builder
    @Data
    @AllArgsConstructor
    public static class Account {
        private String accountId;
        private String accountType;
    }
}
