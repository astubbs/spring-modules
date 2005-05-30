package org.springmodules.beans.factory.drivers;

import java.util.Collection;

import org.springframework.context.ApplicationContext;

public interface ApplicationContextDriver {

	public ApplicationContext getApplicationContext(Collection beanReferences, ApplicationContext parent);
}
