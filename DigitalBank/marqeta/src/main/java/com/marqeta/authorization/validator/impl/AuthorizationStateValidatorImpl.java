/* BlankFactor (C)2023 */
package com.marqeta.authorization.validator.impl;

import com.marqeta.authorization.dto.AuthorizationState;
import com.marqeta.authorization.validator.AuthorizationStateValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class AuthorizationStateValidatorImpl
        implements ConstraintValidator<AuthorizationStateValidator, String> {

    @Override
    public boolean isValid(String state, ConstraintValidatorContext constraintValidatorContex) {
        return Arrays.stream(AuthorizationState.values())
                .anyMatch((authorizationState -> authorizationState.getValue().equals(state)));
    }
}
