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
package org.springmodules.validation.valang.parser;

import javax.servlet.ServletContext;

import org.apache.commons.collections.Predicate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.JdkVersion;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.ServletContextAware;
import org.springmodules.util.dateparser.DefaultDateParser;
import org.springmodules.validation.valang.ValangException;
import org.springmodules.validation.valang.functions.AbstractFunction;
import org.springmodules.validation.valang.functions.Function;
import org.springmodules.validation.valang.functions.InRoleFunction;
import org.springmodules.validation.valang.functions.LengthOfFunction;
import org.springmodules.validation.valang.functions.LowerCaseFunction;
import org.springmodules.validation.valang.functions.NotFunction;
import org.springmodules.validation.valang.functions.RegExFunction;
import org.springmodules.validation.valang.functions.ResolveFunction;
import org.springmodules.validation.valang.functions.UpperCaseFunction;
import org.springmodules.validation.valang.predicates.GenericTestPredicate;
import org.springmodules.validation.valang.predicates.Operator;

/**
 * <p>Allows registration of custom functions. Custom functions can overwrite default functions.
 * 
 * <p>Default functions are:
 * 
 * <ul>
 * 	<li>len, length, size
 * 	<li>upper
 * 	<li>lower
 * 	<li>!
 * </ul> 
 * 
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public class DefaultVisitor implements ValangVisitor, BeanFactoryAware, ApplicationContextAware, ResourceLoaderAware, MessageSourceAware, ApplicationEventPublisherAware, ServletContextAware {

    private ValangVisitor visitor = null;
    private DefaultDateParser dateParser = null;

    private BeanFactory beanFactory = null;
    private ApplicationContext applicationContext = null;
    private ResourceLoader resourceLoader = null;
    private MessageSource messageSource = null;
    private ApplicationEventPublisher applicationEventPublisher = null;
    private ServletContext servletContext = null;

    public DefaultVisitor() {
        super();
        if (JdkVersion.getMajorJavaVersion() != JdkVersion.JAVA_13) {
            this.dateParser = new DefaultDateParser();
        }
    }

    public Function getFunction(String name, Function[] arguments, int line, int column) {
        return lifeCycleCallbacks(doGetFunction(name, arguments, line, column), line, column);
    }

    private Function doGetFunction(String name, Function[] arguments, int line, int column) {
        if (getVisitor() != null) {
            Function function = getVisitor().getFunction(name, arguments, line, column);
            if (function != null) {
                return function;
            }
        }

        if ("len".equals(name)) {
            return new LengthOfFunction(arguments, line, column);
        } else if ("length".equals(name)) {
            return new LengthOfFunction(arguments, line, column);
        } else if ("size".equals(name)) {
            return new LengthOfFunction(arguments, line, column);
        } else if ("count".equals(name)) {
            return new LengthOfFunction(arguments, line, column);
        } else if ("upper".equals(name)) {
            return new UpperCaseFunction(arguments, line, column);
        } else if ("lower".equals(name)) {
            return new LowerCaseFunction(arguments, line, column);
        } else if ("!".equals(name)) {
            return new NotFunction(arguments, line, column);
        } else if ("resolve".equals(name)) {
            return new ResolveFunction(arguments, line, column);
        } else if ("match".equals(name)) {
            return new RegExFunction(arguments, line, column);
        } else if ("matches".equals(name)) {
            return new RegExFunction(arguments, line, column);
        } else if ("inRole".equals(name)) {
            return new InRoleFunction(arguments, line, column);
        }

        throw new ValangException("Could not find function [" + name + "]", line, column);
    }

    private Function lifeCycleCallbacks(Function function, int line, int column) {
        if (function instanceof BeanFactoryAware) {
            ((BeanFactoryAware)function).setBeanFactory(beanFactory);
        }
        if (function instanceof ApplicationContextAware) {
            ((ApplicationContextAware)function).setApplicationContext(applicationContext);
        }
        if (function instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware)function).setResourceLoader(resourceLoader);
        }
        if (function instanceof MessageSourceAware) {
            ((MessageSourceAware)function).setMessageSource(messageSource);
        }
        if (function instanceof ApplicationEventPublisherAware) {
            ((ApplicationEventPublisherAware)function).setApplicationEventPublisher(applicationEventPublisher);
        }
        if (function instanceof ServletContextAware) {
            ((ServletContextAware)function).setServletContext(servletContext);
        }
        if (function instanceof AbstractFunction) {
            AbstractFunction abstractFunction = (AbstractFunction)function;
            AutowireCapableBeanFactory autowireCapableBeanFactory = null;

            if (abstractFunction.isAutowireByName() || abstractFunction.isAutowireByType()) {
                if (applicationContext instanceof ConfigurableApplicationContext) {
                    ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext)applicationContext;
                    autowireCapableBeanFactory = configurableApplicationContext.getBeanFactory();
                } else if (beanFactory instanceof AutowireCapableBeanFactory) {
                    autowireCapableBeanFactory = (AutowireCapableBeanFactory)beanFactory;
                } else if (applicationContext == null && beanFactory == null) {
                    throw new ValangException("Could not autowire function: no application context or bean factory available", line, column);
                } else {
                    throw new ValangException("Could not autowire function: application context or bean factory does not support autowiring", line, column);
                }
            }
            if (abstractFunction.isAutowireByName()) {
                autowireCapableBeanFactory.autowireBeanProperties(function, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
            }
            if (abstractFunction.isAutowireByType()) {
                autowireCapableBeanFactory.autowireBeanProperties(function, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
            }
            try {
                abstractFunction.init();
            } catch (Exception e) {
                throw new ValangException("Error initializing function", e, line, column);
            }
        }
        return function;
    }

    /**
     * <p>Register a custom visitor to look up custom functions. Lookup of functions
     * will first be delegated to this visitor. If no function has been returned (null)
     * lookup will be handled by DefaultVisitor.
     *
     * @param visitor the custom visitor
     */
    public void setVisitor(ValangVisitor visitor) {
        this.visitor = visitor;
    }

    public ValangVisitor getVisitor() {
        return this.visitor;
    }

    public Predicate getPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column) {
        return new GenericTestPredicate(leftFunction, operator, rightFunction, line, column);
    }

    public DefaultDateParser getDateParser() {
        if (this.dateParser == null) {
            throw new IllegalStateException("Date parser not supported in Java 1.3 or older.");
        }
        return this.dateParser;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
