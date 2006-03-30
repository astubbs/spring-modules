package org.springmodules.beans.factory.drivers.xml;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springmodules.beans.factory.drivers.WebApplicationContextDriver;

public class XmlWebApplicationContextDriver extends XmlApplicationContextDriver
		implements WebApplicationContextDriver {

	public XmlWebApplicationContextDriver() {
		super();
	}

	public WebApplicationContext getWebApplicationContext(
			Collection beanReferences, ApplicationContext parent) {
		XmlWebApplicationContext wac = (XmlWebApplicationContext) getApplicationContext(beanReferences,
				parent);
		wac.refresh();
		return wac;
	}

	protected ApplicationContext createApplicationContext(
			final ApplicationContext parent, final Resource resource) {
		return new XmlWebApplicationContext() {
			{
				setParent(parent);
			}

			protected void loadBeanDefinitions(
					org.springframework.beans.factory.xml.XmlBeanDefinitionReader reader)
					throws org.springframework.beans.BeansException,
					java.io.IOException {
				reader.loadBeanDefinitions(resource);
			}
		};
	}
	
	public WebApplicationContext getWebApplicationContext(
			final Collection beanReferences, final ApplicationContext parent,
			final ServletContext servletContext, final String namespace,
			final String[] configLocations) {
		XmlWebApplicationContext wac = (XmlWebApplicationContext)getApplicationContext(beanReferences, parent);
		wac.setServletContext(servletContext);
		wac.setNamespace(namespace);
		if (configLocations != null) {
			wac.setConfigLocations(configLocations);
		}
		wac.refresh();
		return wac;
	}

}
