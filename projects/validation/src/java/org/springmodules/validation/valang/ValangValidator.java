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
import org.springmodules.validation.util.BasicContextAware;
import org.springmodules.validation.util.date.DefaultDateParser;
import org.springmodules.validation.util.date.DefaultDateParser.DateModifier;
import org.springmodules.validation.valang.parser.ValangBased;
import org.springmodules.validation.valang.parser.ValangParser;
import org.springmodules.validation.valang.predicates.ValidationRule;

/**
 * <p>
 * An implementation of <code>Validator</code> that takes a Valang syntax string 
 * to define the set of validation rules it will apply. This instance is thread-safe.
 * <p>
 * The syntax of a Valang instruction is:
 * 
 * <pre>
 * 
 *  { &lt;key&gt; : &lt;expression&gt; : &lt;error_message&gt; [ : &lt;error_key&gt; [ : &lt;error_args&gt; ] ] }
 *  
 * </pre>
 * 
 * <p>
 * These instructions can be repeated and will be combined in a Validator
 * instance. Each instruction will execute the expression on a target bean. If
 * the expression fails the key will be rejected with the error message, error
 * key and error arguments. If no error key is provided the key will be used as
 * error key.
 * <p>
 * Some examples of the Valang syntax:
 * 
 * <pre>
 * 
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
 *  
 * </pre>
 * 
 * <p>
 * Custom property editors can be registered using
 * org.springmodules.validation.valang.CustomPropertyEditor.
 * <p>
 * A custom visitor can be registered to use custom functions in the Valang
 * syntax.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.springmodules.validation.util.date.DefaultDateParser
 * @see org.springframework.validation.Validator
 */

public class ValangValidator extends BasicContextAware implements ValangBased, Validator, InitializingBean {

    private String valang = null;

    private Collection customPropertyEditors = null;

    private Map dateParserRegistrations = null;

    private Map customFunctions = null;

    private Collection rules = null;

    public ValangValidator() {
        super();
    }

    /**
     * <p>
     * This property sets the Valang syntax.
     * 
     * @param valang
     *            the Valang syntax
     */
    public void setValang(String valang) {
        this.valang = valang;
    }

    /**
     * <p>
     * Sets custom property editors on BeanWrapper instances (optional).
     * 
     * @param customPropertyEditors
     *            the custom editors.
     * @see BeanWrapper#registerCustomEditor(java.lang.Class, java.lang.String,
     *      java.beans.PropertyEditor)
     * @see BeanWrapper#registerCustomEditor(java.lang.Class,
     *      java.beans.PropertyEditor)
     */
    public void setCustomPropertyEditors(Collection customPropertyEditors) {
        this.customPropertyEditors = customPropertyEditors;
    }

    /**
     * <p>
     * Sets date parser registrations (formats and modifiers) on
     * DefaultDateParser (optional).
     * 
     * @param dateParserRegistrations
     *            the date parser registrations
     * @see DefaultDateParser#register(String,
     *      String)
     * @see DefaultDateParser#register(String,
     *      DateModifier)
     * @deprecated Use {@link #setDateParsers(java.util.Map)} instead.
     */
    public void setDateParserRegistrations(Map dateParserRegistrations) {
        setDateParsers(dateParserRegistrations);
    }

    private Map getDateParserRegistrations() {
        return this.dateParserRegistrations;
    }

    public void setDateParsers(Map parserByRegexp) {
        this.dateParserRegistrations = parserByRegexp;
    }

    /**
     * <p>
     * Takes a map with function names and function class names. Function classes
     * must have a public constructor with a single org.springmodules.validation.valang.functions.Function
     * parameter.
     * 
     * <p>
     * These custom functions can be combined with a separate visitor. A function will first be looked
     * up in this map if present, then in the custom visitor if present and then in the default functions.
     * 
     * @param customFunctions map with custom functions
     */
    public void setCustomFunctions(Map customFunctions) {
        this.customFunctions = customFunctions;
    }

    private Map getCustomFunctions() {
        return this.customFunctions;
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

        ValangParser parser = new ValangParser(getValang());

        parser.getVisitor().setApplicationContext(applicationContext);
        parser.getVisitor().setBeanFactory(beanFactory);
        parser.getVisitor().setApplicationEventPublisher(applicationEventPublisher);
        parser.getVisitor().setMessageSource(messageSource);
        parser.getVisitor().setResourceLoader(resourceLoader);
        parser.getVisitor().setServletContext(servletContext);

        parser.setDateParsersByRegexp(getDateParserRegistrations());
        parser.setFunctionsByName(getCustomFunctions());

        rules = parser.parseValidation();

    }

    public boolean supports(Class clazz) {
        return true;
    }

    public void validate(Object target, Errors errors) {

        BeanWrapper beanWrapper = (target instanceof BeanWrapper) ? (BeanWrapper)target : new BeanWrapperImpl(target);

        if (getCustomPropertyEditors() != null) {
            for (Iterator iter = getCustomPropertyEditors().iterator(); iter.hasNext();) {
                CustomPropertyEditor customPropertyEditor = (CustomPropertyEditor)iter.next();

                if (customPropertyEditor.getRequiredType() == null) {
                    throw new IllegalArgumentException("[requiredType] is required on CustomPropertyEditor instances!");
                }
                else if (customPropertyEditor.getPropertyEditor() == null) {
                    throw new IllegalArgumentException(
                            "[propertyEditor] is required on CustomPropertyEditor instances!");
                }

                if (StringUtils.hasLength(customPropertyEditor.getPropertyPath())) {
                    beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(),
                            customPropertyEditor.getPropertyPath(), customPropertyEditor.getPropertyEditor());
                }
                else {
                    beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(),
                            customPropertyEditor.getPropertyEditor());
                }
            }
        }

        for (Iterator iter = rules.iterator(); iter.hasNext();) {
            ValidationRule rule = (ValidationRule)iter.next();
            rule.validate(beanWrapper, errors);
        }
    }
}