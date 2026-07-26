/* BlankFactor (C)2023 */
package com.accelerator.authorization.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationResponse {

    private String token;
    private String message;
}
