/* BlankFactor (C)2023 */
package com.marqeta.authorization.validator;

import static java.lang.annotation.ElementType.TYPE;

import com.marqeta.authorization.validator.impl.PrecedingTransactionTokenValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrecedingTransactionTokenValidatorImpl.class)
public @interface PrecedingTransactionTokenValidator {

    String message() default "preceding_transaction_token is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
