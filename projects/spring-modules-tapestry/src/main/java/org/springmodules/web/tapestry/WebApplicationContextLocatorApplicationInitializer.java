package org.springmodules.web.tapestry;

import org.apache.hivemind.lib.SpringBeanFactoryHolder;
import org.apache.tapestry.services.ApplicationInitializer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 * Created by IntelliJ IDEA.
 * User: stevendevijver
 * Date: Oct 16, 2005
 * Time: 2:55:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebApplicationContextLocatorApplicationInitializer implements ApplicationInitializer {
    private SpringBeanFactoryHolder springBeanFactoryHolder = null;

    public void setSpringBeanFactoryHolder(SpringBeanFactoryHolder springBeanFactoryHolder) {
        this.springBeanFactoryHolder = springBeanFactoryHolder;
    }

    public void initialize(HttpServlet servlet) {
        ServletContext servletContext = servlet.getServletContext();
        BeanFactory beanFactory = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.springBeanFactoryHolder.setBeanFactory(beanFactory);
    }
}
