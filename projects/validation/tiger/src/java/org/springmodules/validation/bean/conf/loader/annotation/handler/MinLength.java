package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springmodules.validation.bean.rule.MinLengthValidationRule;

/**
 * Represents a validation rule that checks whether a string has a minimum amount of characters.
 *
 * @author Uri Boness
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@ValidationRule
public @interface MinLength {

    /**
     * Represents the lower bound for the length of the validated instance.<br/>
     * Note: since <code>length</code> is a scalar value, it makes no sense to set the lower bound to
     * a negative value.
     */
    int value() default 0;

    /**
     * Returns the error code that represents the error when the validation fails.
     */
    String errorCode() default MinLengthValidationRule.DEFAULT_ERROR_CODE;

    /**
     * Returns the default message that represents the error when the validation fails.
     */
    String message() default MinLengthValidationRule.DEFAULT_ERROR_CODE;

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
