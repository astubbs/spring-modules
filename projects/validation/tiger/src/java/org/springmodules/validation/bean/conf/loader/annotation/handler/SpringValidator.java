package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.lang.annotation.*;

/**
 * A class level annotation that defines a validator for the annotated class.
 *
 * @author Uri Boness
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ValidationRule
public @interface SpringValidator {

    /**
     * The name of the validator as defined in the spring application context.
     */
    String beanName();

}
