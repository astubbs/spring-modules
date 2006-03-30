package org.springmodules.beans.factory.drivers;

public interface Alias {
	
	public BeanReference getReferencedBean();
	
	public String getAlias();
}
