/* BlankFactor (C)2023 */
package com.blankfactor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionStateEnum {
    PENDING,
    COMPLETION,
    CLEARED
}
