/* BlankFactor (C)2023 */
package com.marqeta.authorization.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationResponse {
    @JsonProperty("jit_funding")
    private JitFunding jitFunding;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JitFunding {
        private String token;
        private String method;

        @JsonProperty("user_token")
        private String userToken;

        @JsonProperty("acting_user_token")
        private String actingUserToken;

        private BigDecimal amount;
        private String memo;
        private String tags;

        @JsonProperty("decline_reason")
        private String declineReason;
    }
}
