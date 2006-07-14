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

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.parser.valang.ValangConditionParser;
import org.springmodules.validation.valang.parser.ValangBased;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.ResourceLoader;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} implementation that handles {@link Valang} annnotations.
 *
 * @author Uri Boness
 */
public class ValangPropertyValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler
    implements ValangBased {

    private ValangConditionParser parser;

    /**
     * Constructs a new ValangPropertyValidationAnnotationHandler.
     */
    public ValangPropertyValidationAnnotationHandler() {
        super(Valang.class);
        parser = new ValangConditionParser();
    }

    /**
     * The extracted condition is globally scoped if the {@link org.springmodules.validation.bean.conf.annotation.handler.Valang#scope()}
     * attribute is set to {@link ExpressionScope#CONTAINING_OBJECT}.
     *
     * @see org.springmodules.validation.bean.conf.annotation.handler.AbstractPropertyValidationAnnotationHandler#isConditionGloballyScoped(java.lang.annotation.Annotation)
     */
    protected boolean isConditionGloballyScoped(Annotation annotation) {
        Valang valang = (Valang)annotation;
        return valang.scope() == ExpressionScope.CONTAINING_OBJECT;
    }

    /**
     *
     */
    protected Condition extractCondition(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        Valang valang = (Valang)annotation;
        return parser.parse(valang.value());
    }

    /**
     * Returns the valang condition parser this handler uses to parse the valang expressions.
     *
     * @return The valang condition parser this handler uses to parse the valang expressions.
     */
    public ValangConditionParser getParser() {
        return parser;
    }

    /**
     * Sets the valang condition parser this handler will use to parse the valang expressions.
     *
     * @param parser The valang condition parser this handler will use to parse the valang expressions.
     */
    public void setParser(ValangConditionParser parser) {
        this.parser = parser;
    }



    /**
     * Adds the a new custom function to be used in the valang el.
     *
     * @param functionName      The name of the function.
     * @param functionClassName The fully qualified class name of the function.
     */
    public void addCustomFunction(String functionName, String functionClassName) {
        parser.addCustomFunction(functionName, functionClassName);
    }

    /**
     * Sets custom functions that should be registered with the used {@link org.springmodules.validation.valang.parser.ValangParser}.
     *
     * @param functionByName the custom functions where the key is the function name and the value is the function
     *                       class FQN.
     */
    public void setCustomFunctions(Map functionByName) {
        parser.setCustomFunctions(functionByName);
    }

    /**
     * Sets the date parser that should be registered with the used {@link org.springmodules.validation.valang.parser.ValangParser}.
     *
     * @param parserByRegexp the date parsers where the key is the regexp associated with the parser and the value
     *                       is the parser class FQN.
     */
    public void setDateParsers(Map parserByRegexp) {
        parser.setDateParsers(parserByRegexp);
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     * Invoked after ResourceLoaderAware's setResourceLoader.
     *
     * @param applicationContext ApplicationContext object to be used by this object
     * @throws org.springframework.context.ApplicationContextException
     *          in case of applicationContext initialization errors
     * @throws org.springframework.beans.BeansException
     *          if thrown by application applicationContext methods
     * @see org.springframework.beans.factory.BeanInitializationException
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        parser.setApplicationContext(applicationContext);
    }

    /**
     * Callback that supplies the owning factory to a bean instance.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     *
     * @param beanFactory owning BeanFactory (may not be <code>null</code>).
     *                    The bean can immediately call methods on the factory.
     * @throws org.springframework.beans.BeansException
     *          in case of initialization errors
     * @see org.springframework.beans.factory.BeanInitializationException
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        parser.setBeanFactory(beanFactory);
    }

    /**
     * Set the ResourceLoader that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     * Invoked before ApplicationContextAware's setApplicationContext.
     *
     * @param resourceLoader ResourceLoader object to be used by this object
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        parser.setResourceLoader(resourceLoader);
    }

    /**
     * Set the MessageSource that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     * Invoked before ApplicationContextAware's setApplicationContext.
     *
     * @param messageSource message sourceto be used by this object
     */
    public void setMessageSource(MessageSource messageSource) {
        parser.setMessageSource(messageSource);
    }

    /**
     * Set the ServletContext that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     * Invoked after ApplicationContextAware's setApplicationContext.
     *
     * @param servletContext ServletContext object to be used by this object
     */
    public void setServletContext(ServletContext servletContext) {
        parser.setServletContext(servletContext);
    }

    /**
     * Set the ApplicationEventPublisher that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method.
     * Invoked before ApplicationContextAware's setApplicationContext.
     *
     * @param applicationEventPublisher event publisher to be used by this object
     */
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        parser.setApplicationEventPublisher(applicationEventPublisher);
    }
}
