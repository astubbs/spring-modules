package org.springmodules.commons.collections.functors;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>UniquePredicateFactoryBean returns instance of
 * org.apache.commons.collections.functors.UniquePredicate.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.UniquePredicate
 */
public class UniquePredicateFactoryBean implements FactoryBean {

	public UniquePredicateFactoryBean() {
		super();
	}

	/**
	 * @return an instance of org.apache.commons.collections.functors.UniquePredicate.
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.UniquePredicate#getInstance()
	 */
	public Object getObject() throws Exception {
		return UniquePredicate.getInstance();
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
