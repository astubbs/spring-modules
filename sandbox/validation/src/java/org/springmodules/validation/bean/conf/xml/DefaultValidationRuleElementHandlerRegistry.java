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

package org.springmodules.validation.bean.conf.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.beans.PropertyDescriptor;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.validation.bean.conf.xml.handler.DateInFutureRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.DateInPastRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.EmailRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.LengthRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.NotBlankRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.NotEmptyRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.NotNullRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.RangeRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.RegExpRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.SizeRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.ValangPropertyValidationElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.ValangClassValidationElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.ValidatorValidationElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.jodatime.InstantInPastRuleElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.jodatime.InstantInFutureRuleElementHandler;
import org.springmodules.validation.util.BasicContextAware;
import org.springmodules.validation.util.LibraryUtils;
import org.springmodules.validation.util.bel.BeanExpressionResolver;
import org.springmodules.validation.util.bel.BeanExpressionResolverAware;
import org.springmodules.validation.util.bel.resolver.ValangFunctionExpressionResolver;
import org.springmodules.validation.util.condition.parser.ConditionParser;
import org.springmodules.validation.util.condition.parser.ConditionParserAware;
import org.springmodules.validation.util.condition.parser.valang.ValangConditionParser;
import org.w3c.dom.Element;

/**
 * A default implementation of {@link ValidationRuleElementHandlerRegistry}. The order in which the the handlers are
 * registered with this registry is meaningful. The last to register will be the first to be checked for support.<br/>
 * This registry is pre-configured with the following handlers:
 * <ol>
 *  <li>{@link NotNullRuleElementHandler}</li>
 *  <li>{@link LengthRuleElementHandler}</li>
 *  <li>{@link NotBlankRuleElementHandler}</li>
 *  <li>{@link EmailRuleElementHandler}</li>
 *  <li>{@link RegExpRuleElementHandler}</li>
 *  <li>{@link SizeRuleElementHandler}</li>
 *  <li>{@link NotEmptyRuleElementHandler}</li>
 *  <li>{@link NotBlankRuleElementHandler}</li>
 *  <li>{@link RangeRuleElementHandler}</li>
 *  <li>{@link ValangPropertyValidationElementHandler}</li>
 *  <li>{@link DateInPastRuleElementHandler}</li>
 *  <li>{@link DateInFutureRuleElementHandler}</li>
 * </ol>
 *
 * @author Uri Boness
 */
public class DefaultValidationRuleElementHandlerRegistry extends BasicContextAware
    implements ValidationRuleElementHandlerRegistry, BeanExpressionResolverAware, ConditionParserAware, InitializingBean {

    private List classHandlers;
    private List propertyHandlers;
    private BeanExpressionResolver expressionResolver;
    private ConditionParser conditionParser;

    /**
     * Constructs a new DefaultValidationRuleElementHandlerRegistry with the default handlers.
     */
    public DefaultValidationRuleElementHandlerRegistry() {
        classHandlers = new ArrayList();
        propertyHandlers = new ArrayList();
        expressionResolver = new ValangFunctionExpressionResolver();
        conditionParser = new ValangConditionParser();
        registerDefaultHandlers();
    }

    /**
     * Registers the given class handler with this registry. The registered handler is registered in such a way that it will
     * be checked first for support (LIFC - Last In First Checked).
     */
    public void registerClassHandler(ClassValidationElementHandler handler) {
        classHandlers.add(0, handler);
    }

    /**
     * @see ValidationRuleElementHandlerRegistry#findClassHandler(org.w3c.dom.Element, Class)
     */
    public ClassValidationElementHandler findClassHandler(Element element, Class clazz) {
        for (Iterator iter = classHandlers.iterator(); iter.hasNext();) {
            ClassValidationElementHandler handler = (ClassValidationElementHandler)iter.next();
            if (handler.supports(element, clazz)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * Registers the given property handler with this registry. The registered handler is registered in such a way that
     * it will be checked first for support. (LIFC - Last In First Checked).
     */
    public void registerPropertyHandler(PropertyValidationElementHandler handler) {
        propertyHandlers.add(0, handler);
    }

    /**
     * @see ValidationRuleElementHandlerRegistry#findPropertyHandler(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public PropertyValidationElementHandler findPropertyHandler(Element element, Class clazz, PropertyDescriptor descriptor) {
        for (Iterator iter = propertyHandlers.iterator(); iter.hasNext();) {
            PropertyValidationElementHandler handler = (PropertyValidationElementHandler)iter.next();
            if (handler.supports(element, clazz, descriptor)) {
                return handler;
            }
        }
        return null;
    }

    public void afterPropertiesSet() throws Exception {

        initLifecycle(conditionParser);
        initLifecycle(expressionResolver);

        for (Iterator iter = classHandlers.iterator(); iter.hasNext();) {
            ClassValidationElementHandler handler = (ClassValidationElementHandler)iter.next();
            if (ConditionParserAware.class.isInstance(handler) && conditionParser != null) {
                ((ConditionParserAware)handler).setConditionParser(conditionParser);
            }
            if (BeanExpressionResolverAware.class.isInstance(handler) && expressionResolver != null) {
                ((BeanExpressionResolverAware)handler).setBeanExpressionResolver(expressionResolver);
            }
            initLifecycle(handler);
        }

        for (Iterator iter = propertyHandlers.iterator(); iter.hasNext();) {
            PropertyValidationElementHandler handler = (PropertyValidationElementHandler)iter.next();
            if (ConditionParserAware.class.isInstance(handler) && conditionParser != null) {
                ((ConditionParserAware)handler).setConditionParser(conditionParser);
            }
            if (BeanExpressionResolverAware.class.isInstance(handler) && expressionResolver != null) {
                ((BeanExpressionResolverAware)handler).setBeanExpressionResolver(expressionResolver);
            }
            initLifecycle(handler);
        }
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Registeres the given class handlers with this registry.
     *
     * @param handlers The handlers to register with this registry.
     */
    public void setExtraClassHandlers(ClassValidationElementHandler[] handlers) {
        for (int i=handlers.length-1; i>=0; i--) {
            registerClassHandler(handlers[i]);
        }
    }

    /**
     * Resets the class handlers in this registry with the given ones.
     *
     * @param handlers The class handlers to be registered with this registry.
     */
    public void setClassHandlers(ClassValidationElementHandler[] handlers) {
        classHandlers.clear();
        setExtraClassHandlers(handlers);
    }

    /**
     * Registeres the given property handlers with this registry.
     *
     * @param handlers The handlers to register with this registry.
     */
    public void setExtraPropertyHandlers(PropertyValidationElementHandler[] handlers) {
        for (int i=handlers.length-1; i>=0; i--) {
            registerPropertyHandler(handlers[i]);
        }
    }

    /**
     * Resets the property handlers in this registry to the given ones (overriding the existing ones).
     *
     * @param handlers The property handlers to register with this registry.
     */
    public void setPropertyHandlers(PropertyValidationElementHandler[] handlers) {
        propertyHandlers.clear();
        setExtraPropertyHandlers(handlers);
    }

    /**
     * Return all class handlers that are registered with this registry.
     *
     * @return All class handlers that are registered with this registry.
     */
    public ClassValidationElementHandler[] getClassHandlers() {
        return (ClassValidationElementHandler[])classHandlers.toArray(new ClassValidationElementHandler[classHandlers.size()]);
    }

    /**
     * Return all property handlers that are registered with this registry.
     *
     * @return All property handlers that are registered with this registry.
     */
    public PropertyValidationElementHandler[] getPropertyHandlers() {
        return (PropertyValidationElementHandler[])propertyHandlers.toArray(new PropertyValidationElementHandler[propertyHandlers.size()]);
    }

    /**
     * Sets the {@link org.springmodules.validation.util.bel.BeanExpressionResolver} to be used.
     *
     * @param resolver The bean expression resolver to be used.
     */
    public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
        this.expressionResolver = resolver;
    }

    /**
     * Returns the used {@link org.springmodules.validation.util.bel.BeanExpressionResolver}.
     *
     * @return The used bean expression resolver.
     */
    public BeanExpressionResolver getBeanExpressionResolver() {
        return expressionResolver;
    }

    /**
     * Returns the used {@link org.springmodules.validation.util.condition.parser.ConditionParser}.
     *
     * @return The used condition parser.
     */
    public ConditionParser getConditionParser() {
        return conditionParser;
    }

    /**
     * Sets the {@link org.springmodules.validation.util.condition.parser.ConditionParser} to be used.
     *
     * @param conditionParser The condition parser to be used.
     */
    public void setConditionParser(ConditionParser conditionParser) {
        this.conditionParser = conditionParser;
    }


    //=============================================== Helper Methods ===================================================

    protected void registerDefaultHandlers() {

        // registering class handlers
        registerClassHandler(new ValangClassValidationElementHandler());
        registerClassHandler(new ValidatorValidationElementHandler());

        // registering property handlers
        registerPropertyHandler(new NotNullRuleElementHandler());
        registerPropertyHandler(new LengthRuleElementHandler());
        registerPropertyHandler(new EmailRuleElementHandler());
        registerPropertyHandler(new RegExpRuleElementHandler());
        registerPropertyHandler(new SizeRuleElementHandler());
        registerPropertyHandler(new NotEmptyRuleElementHandler());
        registerPropertyHandler(new NotBlankRuleElementHandler());
        registerPropertyHandler(new RangeRuleElementHandler());
        registerPropertyHandler(new ValangPropertyValidationElementHandler());
        registerPropertyHandler(new DateInPastRuleElementHandler());
        registerPropertyHandler(new DateInFutureRuleElementHandler());
        if (LibraryUtils.JODA_TIME_IN_CLASSPATH) {
            registerPropertyHandler(new InstantInPastRuleElementHandler());
            registerPropertyHandler(new InstantInFutureRuleElementHandler());
        }
    }

}
