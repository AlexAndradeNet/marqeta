/* BlankFactor (C)2023 */
package com.accelerator.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    @JsonProperty("accountId")
    public long accountId;

    @JsonProperty("cardId")
    public String cardId;
}
