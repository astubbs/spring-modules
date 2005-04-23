package org.springmodules.commons.collections.functors;

import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>FalsePredicateFactoryBean returns an instanceo of
 * org.apache.commons.collections.functors.FalsePredicate.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.FalsePredicate
 */
public class FalsePredicateFactoryBean implements FactoryBean {

	public FalsePredicateFactoryBean() {
		super();
	}

	/**
	 * @return an instance of org.apache.commons.collections.functors.FalsePredicate.
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.FalsePredicate#INSTANCE
	 */
	public Object getObject() throws Exception {
		return null;
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
		return false;
	}

}
