package org.springmodules.beans.factory.drivers;

import java.util.Collection;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public interface WebApplicationContextDriver {

	public WebApplicationContext getWebApplicationContext(Collection beanReferences, ApplicationContext parent);
}
