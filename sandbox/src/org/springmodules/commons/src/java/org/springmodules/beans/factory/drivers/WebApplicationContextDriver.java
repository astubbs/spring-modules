package org.springmodules.beans.factory.drivers;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public interface WebApplicationContextDriver {

	public WebApplicationContext getWebApplicationContext(Collection beanReferences, ApplicationContext parent);
	
	public WebApplicationContext getWebApplicationContext(Collection beanReferences, ApplicationContext parent, ServletContext servletContext, String namespace, String[] configLocations);
}
