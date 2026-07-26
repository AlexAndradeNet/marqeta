/* BlankFactor (C)2023 */
package com.accelerator.common.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    public Transaction(
            String transactionToken,
            String transactionType,
            Account account,
            BigDecimal amount,
            String transactionStatus,
            String precedingTransactionToken,
            String payload) {
        this.transactionToken = transactionToken;
        this.transactionType = transactionType;
        this.account = account;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.precedingTransactionToken = precedingTransactionToken;
        this.payload = payload;
        this.transactionDate = Date.from(Instant.now());
    }

    private Long transactionId;
    private String transactionToken;
    private String transactionType;
    private Account account;
    private BigDecimal amount;
    private String transactionStatus;
    private String precedingTransactionToken;
    private Date transactionDate;
    private String payload;
}
