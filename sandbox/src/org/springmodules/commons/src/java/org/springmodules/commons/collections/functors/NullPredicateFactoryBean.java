package org.springmodules.commons.collections.functors;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NullPredicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>NullPredicateFactoryBean returns an instance of
 * org.apache.commons.collections.functors.NullPredicate.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.NullPredicate
 */
public class NullPredicateFactoryBean implements FactoryBean {

	public NullPredicateFactoryBean() {
		super();
	}

	/**
	 * @return an instance of org.apache.commons.collections.functors.NullPredicate.
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.NullPredicate#INSTANCE
	 */
	public Object getObject() throws Exception {
		return NullPredicate.INSTANCE;
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
