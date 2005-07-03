package org.springmodules.beans.factory.drivers;

import java.util.Collection;
import java.util.Map;

public interface Bean extends Instance {

	public Class getClazz();
	
	public boolean isAbstract();
	
	public BeanReference getParent();
	
	public Map getProperties();
	
	public void setProperty(String name, Instance value);
	
	public boolean isSingleton();

	public boolean isLazy();
	
	public Collection getConstructorArguments();
	
	public String getDescription();
	
	public void setDescription(String description);
	
	public String getInitMethod();
	
	public void setInitMethod(String initMethod);
	
	public String getDestroyMethod();
	
	public void setDestroyMethod(String destroyMethod);
	
	public Collection getDependsOn();
	
	public void setDependsOn(Collection dependsOn);
	
	public String getAutowire();
	
	public void setAutowire(String autowire);
	
	public BeanReference getFactoryBean();
	
	public void setFactoryBean(BeanReference factoryBean);
	
	public String getFactoryMethod();
	
	public void setFactoryMethod(String factoryMethod);
}
