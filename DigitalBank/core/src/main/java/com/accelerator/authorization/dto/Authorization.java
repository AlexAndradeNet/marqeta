/* BlankFactor (C)2023 */
package com.accelerator.authorization.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Authorization {

    private String transactionToken;
    private String userToken;
    private String cardToken;
    private String precedingTransactionToken;
    private BigDecimal amount;
    private AuthorizationType type;
    private AuthorizationStatus status;
    private String currency;
    private String timestamp;
    private String details;
    private String scheme;
    private String merchantName;
    private String merchantCity;
    private String merchantCountry;
    private String payload;
}
