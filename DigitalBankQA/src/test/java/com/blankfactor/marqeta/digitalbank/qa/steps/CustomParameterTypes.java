/* BlankFactor (C)2023 */
package com.blankfactor.marqeta.digitalbank.qa.steps;

import com.blankfactor.enums.TransactionStateEnum;
import com.blankfactor.enums.TransactionTypeEnum;
import io.cucumber.java.ParameterType;

public class CustomParameterTypes {
    @ParameterType(".*")
    public TransactionTypeEnum transactionTypeEnum(String value) {
        return TransactionTypeEnum.valueOf(value.toUpperCase());
    }

    @ParameterType(".*")
    public TransactionStateEnum transactionStateEnum(String value) {
        return TransactionStateEnum.valueOf(value.toUpperCase());
    }
}
