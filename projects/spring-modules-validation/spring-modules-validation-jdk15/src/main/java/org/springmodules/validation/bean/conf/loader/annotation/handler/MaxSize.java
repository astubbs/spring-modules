package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.lang.annotation.*;

import org.springmodules.validation.bean.rule.MaxSizeValidationRule;

/**
 * Represents a validation rule that checks whether the size of a colllection or an array is not greater than a
 * specific value.
 *
 * @author Uri Boness
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@ValidationRule
public @interface MaxSize {

    /**
     * The lower bound of the size of the validated instance.
     */
    int value();

    /**
     * Returns the error code that represents the error when the validation fails.
     */
    String errorCode() default MaxSizeValidationRule.DEFAULT_ERROR_CODE;

    /**
     * Returns the default message that represents the error when the validation fails.
     */
    String message() default MaxSizeValidationRule.DEFAULT_ERROR_CODE;

    /**
     * Comma-delimited list of arguments to be attached to the error code
     */
    String args() default "";

    /**
     * An condition expressed in an expression language (e.g. OGNL, Valag) that determines when
     * this validation rule should be applied.
     */
    String applyIf() default "";

    /**
     * A list of context in which this validation rule is applicable. Empty list means this rule is always applicable
     * regardless the validation context.
     */
    String[] contexts() default {};
}
