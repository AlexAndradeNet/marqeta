/* BlankFactor (C)2023 */
package com.accelerator.authorization.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthorizationType {
    AUTHORIZATION("authorization"),
    AUTHORIZATION_REVERSAL("authorization.reversal"),
    AUTHORIZATION_CLEARING("authorization.clearing"),
    INVALID("invalid");

    private final String value;
}
