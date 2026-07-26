/* BlankFactor (C)2023 */
package com.accelerator.common.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NotNull(message = "json body is required")
public class AccountRequest {

    @NotNull(message = "cardId is required")
    @NotEmpty(message = "cardId is required")
    private String cardId;

    @NotNull(message = "accountBalance is required")
    @Min(value = 1, message = "amount should be greater than zero")
    private BigDecimal accountBalance;

    @NotNull(message = "reservationBalance is required")
    private BigDecimal reservationBalance;
}
