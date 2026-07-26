/* BlankFactor (C)2023 */
package com.marqeta.authorization.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorizationErrorResponse {

    private Integer status;
    private String message;
}
