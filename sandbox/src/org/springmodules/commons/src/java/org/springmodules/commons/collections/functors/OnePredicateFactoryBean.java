package org.springmodules.commons.collections.functors;

import java.util.Collection;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.OnePredicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>OnePredicateFactoryBean returns an instance of
 * org.apache.commons.collections.functors.OnePredicate.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.OnePredicate
 */
public class OnePredicateFactoryBean implements FactoryBean {

	private Collection predicates = null;
	
	public OnePredicateFactoryBean() {
		super();
	}

	public void setPredicates(Collection predicates) {
		this.predicates = predicates;
	}
	
	/**
	 * @return an instance of org.apache.commons.collections.functors.OnePredicate
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.OnePredicate#getInstance(java.util.Collection)
	 */
	public Object getObject() throws Exception {
		if (predicates == null) {
			throw new IllegalArgumentException("[predicates] property has not been set!");
		}
		
		return OnePredicate.getInstance(predicates);
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
