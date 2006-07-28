package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springmodules.validation.bean.conf.DefaultValidationErrorCodes;

/**
 * Represents a validation rule that checks whether the size of a colllection or an array is not greater than a
 * specific value.
 *
 * @author Uri Boness
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface MaxSize {

    /**
     * The lower bound of the size of the validated instance.
     */
    int value();

    /**
     * Returns the error code that represents the error when the validation fails.
     */
    String errorCode() default DefaultValidationErrorCodes.MAX_SIZE_ERROR_CODE;

    /**
     * Returns the default message that represents the error when the validation fails.
     */
    String message() default DefaultValidationErrorCodes.MAX_SIZE_ERROR_CODE;

    /**
     * Comma-delimited list of arguments to be attached to the error code
     */
    String args() default "";

    /**
     * An condition expressed in an expression language (e.g. OGNL, Valag) that determines when
     * this validation rule should be applied.
     */
    String applyIf() default "";

}
