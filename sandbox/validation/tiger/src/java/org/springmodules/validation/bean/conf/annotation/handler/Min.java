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

package org.springmodules.validation.bean.conf.annotation.handler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springmodules.validation.bean.conf.xml.handler.RangeRuleElementHandler;

/**
 * Represents a validation rule that checks whether the a numeric instance is greater then (or equals) a specific lower
 * bound.
 *
 * @author Uri Boness
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Min {

    /**
     * The lower bound of the numeric instance value
     */
    double value();

    /**
     * Indicates whether the lower bound is a legal value of the checked value.
     */
    boolean inclusive() default true;

    /**
     * Returns the error code that represents the error when the validation fails.
     */
    String errorCode() default RangeRuleElementHandler.DEFAULT_MIN_ERROR_CODE;

    /**
     * Returns the default message that represents the error when the validation fails.
     */
    String message() default RangeRuleElementHandler.DEFAULT_MIN_ERROR_CODE;

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
