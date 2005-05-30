package org.springmodules.beans.factory.drivers.xml;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springmodules.beans.factory.drivers.ApplicationContextDriver;

public class XmlApplicationContextDriver implements ApplicationContextDriver {

	public XmlApplicationContextDriver() {
		super();
	}

	public ApplicationContext getApplicationContext(Collection beanReferences, ApplicationContext parent) {
		Resource resource = XmlApplicationContextUtils.convert2xml(beanReferences);
		return createApplicationContext(parent, resource);
	}

	protected ApplicationContext createApplicationContext(ApplicationContext parent, final Resource resource) {
		return new AbstractXmlApplicationContext(parent) {
			{
				refresh();
			}
			
			protected String[] getConfigLocations() {
				return new String[] {};
			}
			
			protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
				reader.loadBeanDefinitions(resource);
			}
		};
	}
	
}
