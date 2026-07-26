/* BlankFactor (C)2023 */
package com.marqeta.authorization.validator.impl;

import static java.util.Objects.isNull;

import com.marqeta.authorization.dto.AuthorizationType;
import com.marqeta.authorization.dto.request.AuthorizationRequest;
import com.marqeta.authorization.validator.PrecedingTransactionTokenValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PrecedingTransactionTokenValidatorImpl
        implements ConstraintValidator<PrecedingTransactionTokenValidator, AuthorizationRequest> {

    String defaultMessage;

    @Override
    public void initialize(PrecedingTransactionTokenValidator constraintAnnotation) {
        defaultMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(
            AuthorizationRequest authorizationRequest,
            ConstraintValidatorContext constraintValidatorContext) {

        if ((AuthorizationType.AUTHORIZATION_REVERSAL
                                .getValue()
                                .equals(authorizationRequest.getType())
                        || AuthorizationType.AUTHORIZATION_CLEARING
                                .getValue()
                                .equals(authorizationRequest.getType()))
                && isNull(authorizationRequest.getPrecedingTransactionToken())) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(defaultMessage)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
