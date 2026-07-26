/* BlankFactor (C)2023 */
package com.accelerator.common.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "card_id")
    private String cardId;

    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    @Column(name = "reservation_balance")
    private BigDecimal reservationBalance;
}
