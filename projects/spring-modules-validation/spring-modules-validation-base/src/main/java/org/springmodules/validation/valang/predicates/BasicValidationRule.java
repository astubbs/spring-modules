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

package org.springmodules.validation.valang.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.Predicate;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springmodules.validation.valang.functions.Function;


/**
 * <p>Validation rule implementation that will validate a target
 * bean an return an error message is the validation fails.
 *
 * @author Steven Devijver
 * @since 23-04-2005
 */
public class BasicValidationRule implements ValidationRule {

    private Predicate predicate = null;

    private String field = null;

    private String errorMessage = null;

    private String errorKey = null;

    private Collection errorArgs = null;

    public BasicValidationRule(String field, Predicate predicate, String errorMessage) {
        super();
        setField(field);
        setPredicate(predicate);
        setErrorMessage(errorMessage);
    }

    /*
     * added error key and error args to validation rule
     */
    public BasicValidationRule(String field, Predicate predicate, String errorKey, String errorMessage, Collection errorArgs) {
        this(field, predicate, errorMessage);
        setErrorKey(errorKey);
        setErrorArgs(errorArgs);
    }

    private void setPredicate(Predicate predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicate parameter must not be null!");
        }
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return this.predicate;
    }

    private void setErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            throw new IllegalArgumentException("Error message parameter must not be null!");
        }
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    private void setField(String field) {
        if (field == null) {
            throw new IllegalArgumentException("Field parameter must not be null!");
        }
        this.field = field;
    }

    public String getField() {
        return this.field;
    }

    private void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return this.errorKey;
    }

    private void setErrorArgs(Collection errorArgs) {
        this.errorArgs = errorArgs;
    }

    public Collection getErrorArgs() {
        return this.errorArgs;
    }

    public void validate(Object target, Errors errors) {
        Object tmpTarget;

        if (target instanceof BeanWrapper || target instanceof Map) {
            tmpTarget = target;
        } else {
            tmpTarget = new BeanWrapperImpl(target);
        }
        if (!getPredicate().evaluate(tmpTarget)) {

            /*
            * Take into account error key and error args for localization
            */
            if (StringUtils.hasLength(getErrorKey())) {
                if (getErrorArgs() != null && !getErrorArgs().isEmpty()) {
                    Collection tmpColl = new ArrayList();
                    for (Iterator iter = getErrorArgs().iterator(); iter.hasNext();) {
                        tmpColl.add(((Function) iter.next()).getResult(tmpTarget));
                    }
                    errors.rejectValue(getField(), getErrorKey(), tmpColl.toArray(), getErrorMessage());
                } else {
                    errors.rejectValue(getField(), getErrorKey(), getErrorMessage());
                }
            } else {
                errors.rejectValue(getField(), getField(), getErrorMessage());
            }

        }
    }


}
