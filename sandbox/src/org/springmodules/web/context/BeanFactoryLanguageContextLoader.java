package org.springmodules.web.context;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springmodules.beans.factory.bfl.parser.BFLUtils;
import org.springmodules.beans.factory.drivers.xml.XmlWebApplicationContextDriver;

public class BeanFactoryLanguageContextLoader extends ContextLoader {

	public static final String BFL_CONTEXT_CONFIG_LOCATION = "bflContextConfigLocation";
	
	public static final String CREATE_PARENT_WEB_APPLICATION_CONTEXT = "createParentWebApplicationContext";
	
	public BeanFactoryLanguageContextLoader() {
		super();
	}

	protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent) throws BeansException {
		ApplicationContext secondParent = null;
		String bflContextConfigLocation = servletContext.getInitParameter(BFL_CONTEXT_CONFIG_LOCATION);
		String createParentWebApplicationContext = servletContext.getInitParameter(CREATE_PARENT_WEB_APPLICATION_CONTEXT);
		Collection beanReferences = null;
		
		if (createParentWebApplicationContext != null && createParentWebApplicationContext.toLowerCase().equals("true")) {
			secondParent = super.createWebApplicationContext(servletContext, parent);
		} else {
			secondParent = parent;
		}
		
		if (bflContextConfigLocation != null) {
			beanReferences = BFLUtils.parse(new ClassPathResource(bflContextConfigLocation), secondParent);
		} else {
			beanReferences = BFLUtils.parse(new ClassPathResource("applicationContext.bfl"), secondParent);
		}
		
		return new XmlWebApplicationContextDriver().getWebApplicationContext(beanReferences, secondParent);
	}
}
