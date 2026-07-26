/* BlankFactor (C)2023 */
package com.marqeta.authorization.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NotNull(message = "json body is required")
public class CoreAuthorizationRequest {

    @NotBlank(message = "type is required")
    private String type;

    @NotBlank(message = "status is required")
    private String status;

    @NotBlank(message = "transactionToken is required")
    private String transactionToken;

    @NotBlank(message = "userToken is required")
    private String userToken;

    @NotBlank(message = "cardToken is required")
    private String cardToken;

    private String precedingTransactionToken;

    @NotNull(message = "amount is required")
    @Min(value = 1, message = "amount should be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "currency is required")
    private String currency;

    @NotBlank(message = "timestamp is required")
    private String timestamp;

    private String details;

    @NotBlank(message = "scheme is required")
    private String scheme;

    private String merchantName;

    private String merchantCity;

    private String merchantCountry;

    @NotBlank(message = "payload is required")
    private String payload;
}
