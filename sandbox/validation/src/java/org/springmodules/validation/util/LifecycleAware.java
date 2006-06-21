package org.springmodules.validation.util;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.ServletContextAware;

/**
 * @author Uri Boness
 */
public class LifecycleAware
    implements ApplicationContextAware,
               BeanFactoryAware,
               ResourceLoaderAware,
               MessageSourceAware,
               ServletContextAware,
               ApplicationEventPublisherAware {

    protected ApplicationContext applicationContext = null;
    protected BeanFactory beanFactory = null;
    protected ResourceLoader resourceLoader = null;
    protected MessageSource messageSource = null;
    protected ServletContext servletContext = null;
    protected ApplicationEventPublisher applicationEventPublisher = null;

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

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    protected void initLifecycle(LifecycleAware object) {
        object.setBeanFactory(beanFactory);
        object.setApplicationContext(applicationContext);
        object.setResourceLoader(resourceLoader);
        object.setMessageSource(messageSource);
        object.setApplicationEventPublisher(applicationEventPublisher);
        object.setServletContext(servletContext);
    }

    protected void initLifecycle(Object object) {
        if (object instanceof BeanFactoryAware) {
            ((BeanFactoryAware)object).setBeanFactory(beanFactory);
        }
        if (object instanceof ApplicationContextAware) {
            ((ApplicationContextAware)object).setApplicationContext(applicationContext);
        }
        if (object instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware)object).setResourceLoader(resourceLoader);
        }
        if (object instanceof MessageSourceAware) {
            ((MessageSourceAware)object).setMessageSource(messageSource);
        }
        if (object instanceof ApplicationEventPublisherAware) {
            ((ApplicationEventPublisherAware)object).setApplicationEventPublisher(applicationEventPublisher);
        }
        if (object instanceof ServletContextAware) {
            ((ServletContextAware)object).setServletContext(servletContext);
        }
    }
}
