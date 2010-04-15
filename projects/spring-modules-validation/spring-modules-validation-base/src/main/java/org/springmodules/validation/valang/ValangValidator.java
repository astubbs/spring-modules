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

package org.springmodules.validation.valang;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.util.date.DefaultDateParser;
import org.springmodules.validation.util.date.DefaultDateParser.DateModifier;
import org.springmodules.validation.valang.parser.SimpleValangBased;
import org.springmodules.validation.valang.parser.ValangParser;
import org.springmodules.validation.valang.predicates.ValidationRule;

/**
 * <p/>
 * An implementation of <code>Validator</code> that takes a Valang syntax string
 * to define the set of validation rules it will apply. This instance is thread-safe.
 * <p/>
 * The syntax of a Valang instruction is:
 * <p/>
 * <pre>
 * <p/>
 *  { &lt;key&gt; : &lt;expression&gt; : &lt;error_message&gt; [ : &lt;error_key&gt; [ : &lt;error_args&gt; ] ] }
 * <p/>
 * </pre>
 * <p/>
 * <p/>
 * These instructions can be repeated and will be combined in a Validator
 * instance. Each instruction will execute the expression on a target bean. If
 * the expression fails the key will be rejected with the error message, error
 * key and error arguments. If no error key is provided the key will be used as
 * error key.
 * <p/>
 * Some examples of the Valang syntax:
 * <p/>
 * <pre>
 * <p/>
 *      &lt;bean id=&quot;myValidator&quot; class=&quot;org.springmodules.validation.valang.ValangValidatorFactoryBean&quot;&gt;
 *          &lt;property name=&quot;valang&quot;&gt;&lt;value&gt;&lt;![CDATA[
 *          { age : ? is not null : 'Age is a required field.' : 'age_required' }
 *          { age : ? is null or ? &gt;= minAge : 'Customers must be {0} years or older.' : 'not_old_enough' : minAge }
 *          { valueDate : ? is not null : 'Value date is a required field.' : 'valueDate_required' }
 *          { valueDate : ? is null or (? &gt;= [T&lt;d] and [T&gt;d] &gt; ?) :
 *                  'Value date must be today.' : 'valueDate_today' }
 *          { firstName : ? has text : 'First name is a required field.' : 'firstName_required' }
 *          { firstName : ? has no text or length(firstName) &lt;= 50 :
 *                  'First name must be no longer than {0} characters.' : 'firstName_length' : 50 }
 *          { size : ? has length : 'Size is a required field.' }
 *          { size : ? has no length or upper(?) in 'S', 'M', 'L', 'XL' :
 *                  'Size must be either {0}, {1}, {2} or {3}.' : 'size_error' : 'S', 'M', 'L', 'XL' }
 *          { lastName : ? has text and !(false) = true :
 *                  'Last name is required and not false must be true.' }
 *          ]]&gt;&lt;/value&gt;&lt;/property&gt;
 *      &lt;/bean&gt;
 * <p/>
 * </pre>
 * <p/>
 * <p/>
 * Custom property editors can be registered using
 * org.springmodules.validation.valang.CustomPropertyEditor.
 * <p/>
 * A custom visitor can be registered to use custom functions in the Valang
 * syntax.
 *
 * @author Steven Devijver
 * @see org.springmodules.validation.util.date.DefaultDateParser
 * @see org.springframework.validation.Validator
 * @since 23-04-2005
 */

public class ValangValidator extends SimpleValangBased implements Validator, InitializingBean {

    private String valang = null;

    private Collection customPropertyEditors = null;

    private Collection rules = null;

    public ValangValidator() {
        super();
    }

    /**
     * <p/>
     * This property sets the Valang syntax.
     *
     * @param valang the Valang syntax
     */
    public void setValang(String valang) {
        this.valang = valang;
    }

    /**
     * <p/>
     * Sets custom property editors on BeanWrapper instances (optional).
     *
     * @param customPropertyEditors the custom editors.
     * @see BeanWrapper#registerCustomEditor(Class, String,
     *      java.beans.PropertyEditor)
     * @see BeanWrapper#registerCustomEditor(Class,
     *      java.beans.PropertyEditor)
     */
    public void setCustomPropertyEditors(Collection customPropertyEditors) {
        this.customPropertyEditors = customPropertyEditors;
    }

    /**
     * <p/>
     * Sets date parser registrations (formats and modifiers) on
     * DefaultDateParser (optional).
     *
     * @param dateParserRegistrations the date parser registrations
     * @see DefaultDateParser#register(String,
     *      String)
     * @see DefaultDateParser#register(String,
     *      DateModifier)
     * @deprecated Use {@link #setDateParsers(java.util.Map)} instead.
     */
    public void setDateParserRegistrations(Map dateParserRegistrations) {
        setDateParsers(dateParserRegistrations);
    }

    private Collection getCustomPropertyEditors() {
        return this.customPropertyEditors;
    }

    private String getValang() {
        return this.valang;
    }

    public Collection getRules() {
        return rules;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(getValang(), "'valang' property must be set!");
        ValangParser parser = createValangParser(getValang());
        rules = parser.parseValidation();
    }

    public boolean supports(Class clazz) {
        return true;
    }

    public void validate(Object target, Errors errors) {

        BeanWrapper beanWrapper = (target instanceof BeanWrapper) ? (BeanWrapper) target : new BeanWrapperImpl(target);

        if (getCustomPropertyEditors() != null) {
            for (Iterator iter = getCustomPropertyEditors().iterator(); iter.hasNext();) {
                CustomPropertyEditor customPropertyEditor = (CustomPropertyEditor) iter.next();

                if (customPropertyEditor.getRequiredType() == null) {
                    throw new IllegalArgumentException("[requiredType] is required on CustomPropertyEditor instances!");
                } else if (customPropertyEditor.getPropertyEditor() == null) {
                    throw new IllegalArgumentException(
                        "[propertyEditor] is required on CustomPropertyEditor instances!");
                }

                if (StringUtils.hasLength(customPropertyEditor.getPropertyPath())) {
                    beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(),
                        customPropertyEditor.getPropertyPath(), customPropertyEditor.getPropertyEditor());
                } else {
                    beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(),
                        customPropertyEditor.getPropertyEditor());
                }
            }
        }

        for (Iterator iter = rules.iterator(); iter.hasNext();) {
            ValidationRule rule = (ValidationRule) iter.next();
            rule.validate(beanWrapper, errors);
        }
    }
}