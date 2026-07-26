/* BlankFactor (C)2023 */
package com.marqeta.authorization.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthorizationState {
    PENDING("PENDING"),
    CLEARED("CLEARED"),
    DECLINED("DECLINED"),
    COMPLETION("COMPLETION");

    private final String value;
}
