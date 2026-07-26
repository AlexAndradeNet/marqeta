/* BlankFactor (C)2023 */
package com.accelerator.authorization.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthorizationStatus {
    PENDING("PENDING"),
    CLEARED("CLEARED"),
    DECLINED("DECLINED"),
    COMPLETION("COMPLETION"),
    INACTIVE_CARD("INACTIVE_CARD"),
    INVALID("INVALID");

    private final String value;
}
