/* BlankFactor (C)2023 */
package com.marqeta.authorization.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class Authorization implements Serializable {

    @Serial private static final long serialVersionUID = 1905122041950251208L;

    // Core processing fields
    private String type;
    private String transactionToken;
    private BigDecimal amount;
    private String userToken;
    private String cardToken;
    private String precedingTransactionToken;
    private String status;

    // Ledger processing fields
    private String currency;
    private String timestamp;
    private String details;
    private String scheme;
    private String merchantName;
    private String merchantCity;
    private String merchantCountry;

    // Marqeta processing fields
    private String jitFundingToken;
    private String actingUserToken;
    private String method;
    private String memo;
    private String tags;

    private String payload;
}
