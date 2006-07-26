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

package org.springmodules.validation.bean.conf.xml.handler;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.xml.DefaultXmBeanValidationConfigurationlLoaderConstants;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.parser.valang.ValangConditionParser;
import org.springmodules.validation.valang.parser.SimpleValangBased;
import org.springmodules.validation.valang.parser.ValangBased;
import org.w3c.dom.Element;

/**
 * An {@link AbstractClassValidationElementHandler} that can handle class elements that represent valang validation rules.
 * This handler creates a valang conditoin out of a &lt;valang&gt; element.
 *
 * @author Uri Boness
 */
public class ValangClassValidationElementHandler extends AbstractClassValidationElementHandler
    implements DefaultXmBeanValidationConfigurationlLoaderConstants, ValangBased {

    /**
     * The default error code for the created valang validation rule.
     */
    public static final String DEFAULT_ERROR_CODE = "valang";

    private static final String ELEMENT_NAME = "valang";
    private static final String EXPRESSION_ATTR = "expression";

    private SimpleValangBased valangBased;

    /**
     * Constructs a new ValangPropertyValidationElementHandler.
     */
    public ValangClassValidationElementHandler() {
        super(ValangClassValidationElementHandler.ELEMENT_NAME, DEFAULT_NAMESPACE_URL);
        valangBased = new SimpleValangBased();
    }

    /**
     * Returns {@link #DEFAULT_ERROR_CODE}.
     *
     * @see AbstractClassValidationElementHandler#getDefaultErrorCode(org.w3c.dom.Element)
     */
    protected String getDefaultErrorCode(Element element) {
        return ValangClassValidationElementHandler.DEFAULT_ERROR_CODE;
    }

    /**
     * Creates a valang condition from the given validation rule element.
     *
     * @param element The element that represents the valang validation rule.
     * @return The created valang condition
     * @see AbstractClassValidationElementHandler#extractCondition(org.w3c.dom.Element)
     */
    protected Condition extractCondition(Element element) {
        String expression = element.getAttribute(ValangClassValidationElementHandler.EXPRESSION_ATTR);
        if (!StringUtils.hasText(expression)) {
            throw new XmlConditionConfigurationException("Element '" + ValangClassValidationElementHandler.ELEMENT_NAME + "' must have an 'expression' attribute");
        }
        ValangConditionParser parser = new ValangConditionParser();
        valangBased.initValang(parser);
        return parser.parse(expression);
    }


    //=============================================== Setter/Getter ====================================================

    /**
     * @see org.springmodules.validation.valang.parser.ValangBased#setCustomFunctions(java.util.Map)
     */
    public void setCustomFunctions(Map functionByName) {
        valangBased.setCustomFunctions(functionByName);
    }

    /**
     * @see org.springmodules.validation.valang.parser.ValangBased#addCustomFunction(String, String)
     */
    public void addCustomFunction(String functionName, String functionClassName) {
        valangBased.addCustomFunction(functionName,  functionClassName);
    }

    /**
     * @see org.springmodules.validation.valang.parser.ValangBased#setDateParsers(java.util.Map)
     */
    public void setDateParsers(Map parserByRegexp) {
        valangBased.setDateParsers(parserByRegexp);
    }

    /**
     * @see org.springmodules.validation.valang.parser.ValangBased#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        valangBased.setApplicationContext(applicationContext);
    }

    /**
     * @see org.springmodules.validation.valang.parser.ValangBased#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        valangBased.setBeanFactory(beanFactory);
    }

    /**
     * @see org.springmodules.validation.valang.parser.ValangBased#setResourceLoader(org.springframework.core.io.ResourceLoader)
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        valangBased.setResourceLoader(resourceLoader);
    }

    /**
     * @see org.springmodules.validation.valang.parser.ValangBased#setMessageSource(org.springframework.context.MessageSource)
     */
    public void setMessageSource(MessageSource messageSource) {
        valangBased.setMessageSource(messageSource);
    }

    /**
     * @see org.springmodules.validation.valang.parser.ValangBased#setServletContext(javax.servlet.ServletContext)
     */
    public void setServletContext(ServletContext servletContext) {
        valangBased.setServletContext(servletContext);
    }

    /**
     * @see org.springmodules.validation.valang.parser.ValangBased#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
     */
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        valangBased.setApplicationEventPublisher(applicationEventPublisher);
    }
}
