/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.lang.annotation.*;

import org.springmodules.validation.bean.rule.LengthValidationRule;

/**
 * Represents a validation rule that checks whether the length of a string is within specific
 * bounderies.
 *
 * @author Uri Boness
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@ValidationRule
public @interface Length {

    /**
     * Represents the lower bound for the length of the validated instance.<br/>
     * Note: since <code>length</code> is a scalar value, it makes no sense to set the lower bound to
     * a negative value.
     */
    int min() default 0;

    /**
     * Represents the upper bound for the length of the validated instance.
     */
    int max() default Integer.MAX_VALUE;

    /**
     * Returns the error code that represents the error when the validation fails.
     */
    String errorCode() default LengthValidationRule.DEFAULT_ERROR_CODE;

    /**
     * Returns the default message that represents the error when the validation fails.
     */
    String message() default LengthValidationRule.DEFAULT_ERROR_CODE;

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
