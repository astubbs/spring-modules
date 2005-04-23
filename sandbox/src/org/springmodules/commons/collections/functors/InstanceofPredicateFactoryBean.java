package org.springmodules.commons.collections.functors;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>InstanceofPredicateFactoryBean returns an instance of
 * org.apache.commons.collections.functors.InstanceofPredicate.
 * 
 * @author Steven Devijver
 * @since 23-05-2005
 * @see org.apache.commons.collections.functors.InstanceofPredicate
 */
public class InstanceofPredicateFactoryBean implements FactoryBean {

	private Class type = null;
	
	public InstanceofPredicateFactoryBean() {
		super();
	}

	/**
	 * Sets the instance type.
	 * 
	 * @param type the instance type
	 */
	public void setType(Class type) {
		this.type = type;
	}
	
	/**
	 * @return an instance of org.apache.commons.collections.functors.InstanceofPredicate
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.InstanceofPredicate#getInstance(java.lang.Class)
	 */
	public Object getObject() throws Exception {
		if (type == null) {
			throw new IllegalArgumentException("[type] property has not been set!");
		}
		
		return InstanceofPredicate.getInstance(type);
	}

	/**
	 * @return org.apache.commons.collections.Predicate class.
	 * @see FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Predicate.class;
	}

	/**
	 * @return true
	 * @see FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

}
