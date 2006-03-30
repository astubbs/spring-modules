package org.springmodules.beans.factory.drivers;

public interface BeanReference extends Instance {

	public String getBeanName();
	
	public Bean getBean();
}
