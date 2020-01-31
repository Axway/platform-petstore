package com.axway.core.entitlements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation interface used to label entitlement checking APIs.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EntitlementsValidator.class)
public @interface Entitlements {

    String message() default "Limits reached for provided pet type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
