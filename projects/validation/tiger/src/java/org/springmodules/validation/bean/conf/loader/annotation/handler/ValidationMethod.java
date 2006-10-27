package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.lang.annotation.*;

/**
 * An annotation to be placed on validation rule annotation. This annotation will be used to identify the validation
 * annotations that the framework needs to process.
 *
 * @author Uri Boness
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ValidationRule
public @interface ValidationMethod {

    /**
     * If the validation method validates a specific property, this attribute holds the name of that property.
     */
    String forProperty() default "";

    /**
     * Returns the error code that represents the error when the validation fails.
     */
    String errorCode() default "";

    /**
     * Returns the default message that represents the error when the validation fails.
     */
    String message() default "";

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
