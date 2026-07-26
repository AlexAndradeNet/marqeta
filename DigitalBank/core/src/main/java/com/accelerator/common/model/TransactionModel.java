/* BlankFactor (C)2023 */
package com.accelerator.common.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long transactionId;

    @Column(name = "transaction_token", unique = true)
    private String transactionToken;

    @Column(name = "transaction_type")
    private String transactionType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel account;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "preceding_transaction_token")
    private String precedingTransactionToken;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "payload", columnDefinition = "text")
    private String payload;
}
