package org.springmodules.commons.collections.functors;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>NotNullPredicateFactoryBean returns an instance of
 * org.apache.commons.collections.functors.NotNullPredicate.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.NotNullPredicate
 */
public class NotNullPredicateFactoryBean implements FactoryBean {

	public NotNullPredicateFactoryBean() {
		super();
	}

	/**
	 * @return an instance of org.apache.commons.collections.functors.NotNullPredicate.
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.NotNullPredicate#INSTANCE
	 */
	public Object getObject() throws Exception {
		return NotNullPredicate.INSTANCE;
	}

	/**
	 * @return org.apache.commons.collections.Predicate class
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
