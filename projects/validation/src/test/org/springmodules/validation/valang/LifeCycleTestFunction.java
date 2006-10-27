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

import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springmodules.validation.valang.functions.AbstractFunction;
import org.springmodules.validation.valang.functions.Function;

public class LifeCycleTestFunction extends AbstractFunction implements ApplicationContextAware, BeanFactoryAware, ResourceLoaderAware, MessageSourceAware, ApplicationEventPublisherAware, ServletContextAware {

    private Date timestamp = null;

    private Pattern pattern = null;

    private ApplicationContext applicationContext = null;

    private BeanFactory beanFactory = null;

    private ResourceLoader resourceLoader = null;

    private MessageSource messageSource = null;

    private ApplicationEventPublisher applicationEventPublisher = null;

    private boolean servletContextSet = false;

    private boolean initCalled = false;

    private int patternSetCount = 0;

    public LifeCycleTestFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
    }

    public boolean isAutowireByName() {
        return true;
    }

    public boolean isAutowireByType() {
        return true;
    }

    public void init() throws Exception {
        Assert.notNull(this.timestamp, "timestamp is not set");
        Assert.notNull(this.pattern, "pattern is not set");
        initCalled = true;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
        this.patternSetCount++;
    }

    protected Object doGetResult(Object target) throws Exception {
        LifeCycleBean bean = (LifeCycleBean) getArguments()[0].getResult(target);

        bean.setApplicationContextSet(this.applicationContext != null);
        bean.setBeanFactorySet(this.beanFactory != null);
        bean.setApplicationEventPublisher(this.applicationEventPublisher != null);
        bean.setInitCalled(this.initCalled);
        bean.setMessageSourceSet(this.messageSource != null);
        bean.setResourceLoaderSet(this.resourceLoader != null);
        bean.setServletContextSet(this.servletContextSet);
        bean.setPatternSetCount(this.patternSetCount);

        return bean;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContextSet = true;
    }

}
