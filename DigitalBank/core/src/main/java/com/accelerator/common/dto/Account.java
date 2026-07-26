/* BlankFactor (C)2023 */
package com.accelerator.common.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    private Long accountId;
    private String userId;
    private String cardId;
    private BigDecimal accountBalance;
    private BigDecimal reservationBalance;
}
