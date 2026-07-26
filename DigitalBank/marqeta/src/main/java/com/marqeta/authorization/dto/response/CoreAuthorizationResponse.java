/* BlankFactor (C)2023 */
package com.marqeta.authorization.dto.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoreAuthorizationResponse implements Serializable {

    @Serial private static final long serialVersionUID = 3905122041950251209L;

    private String token;
    private String message;
}
