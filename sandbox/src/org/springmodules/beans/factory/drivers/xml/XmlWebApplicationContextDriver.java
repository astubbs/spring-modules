package org.springmodules.beans.factory.drivers.xml;

import java.util.Collection;

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
		return (WebApplicationContext) getApplicationContext(beanReferences,
				parent);
	}

	protected ApplicationContext createApplicationContext(
			final ApplicationContext parent, final Resource resource) {
		return new XmlWebApplicationContext() {
			{
				setParent(parent);
				refresh();
			}

			protected void loadBeanDefinitions(
					org.springframework.beans.factory.xml.XmlBeanDefinitionReader reader)
					throws org.springframework.beans.BeansException,
					java.io.IOException {
				reader.loadBeanDefinitions(resource);
			}
		};
	}

}
