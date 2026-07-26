/* BlankFactor (C)2023 */
package com.blankfactor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthorizationDTO {
    private String fundingToken;
    private String productToken;
    private String userToken;
    private String cardToken;
    private String transactionToken;
}
