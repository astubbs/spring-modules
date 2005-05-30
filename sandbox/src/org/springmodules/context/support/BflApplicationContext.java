package org.springmodules.context.support;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springmodules.beans.factory.bfl.parser.BFLUtils;
import org.springmodules.beans.factory.drivers.xml.XmlApplicationContextUtils;

public class BflApplicationContext extends AbstractXmlApplicationContext {

	private Resource resource = null;
	
	public BflApplicationContext(Resource resource) {
		super();
		this.resource = resource;
	}

	public BflApplicationContext(Resource resource, ApplicationContext parent) {
		super(parent);
		this.resource = resource;
	}

	protected String[] getConfigLocations() {
		return new String[] {};
	}
	
	protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
		Collection beanReferences = BFLUtils.parse(this.resource, getParent());
		reader.loadBeanDefinitions(XmlApplicationContextUtils.convert2xml(beanReferences));
	}

}
