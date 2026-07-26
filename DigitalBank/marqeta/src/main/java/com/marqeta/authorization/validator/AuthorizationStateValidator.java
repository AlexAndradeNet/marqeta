/* BlankFactor (C)2023 */
package com.marqeta.authorization.validator;

import static java.lang.annotation.ElementType.FIELD;

import com.marqeta.authorization.validator.impl.AuthorizationStateValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthorizationStateValidatorImpl.class)
public @interface AuthorizationStateValidator {

    String message() default
            "Invalid state. Supported stated: PENDING, CLEARED, DECLINED or COMPLETION";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
