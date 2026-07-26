/* BlankFactor (C)2023 */
package com.marqeta.authorization.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NotNull(message = "json body is required")
public class AuthorizationNotificationRequest {

    @NotEmpty(message = "transactions list is required")
    @NotNull(message = "transactions list is required")
    private List<@NotNull(message = "transactions list is required") @Valid AuthorizationRequest>
            transactions;
}
