package org.springmodules.commons.collections.functors;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.TruePredicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>TruePredicateFactoryBean returns an instance of
 * org.apache.commons.collections.functors.TruePredicate.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.TruePredicate
 */
public class TruePredicateFactoryBean implements FactoryBean {

	public TruePredicateFactoryBean() {
		super();
	}

	/**
	 * @return an instance of org.apache.commons.collections.functors.TruePredicate
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.TruePredicate#INSTANCE
	 */
	public Object getObject() throws Exception {
		return TruePredicate.INSTANCE;
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
