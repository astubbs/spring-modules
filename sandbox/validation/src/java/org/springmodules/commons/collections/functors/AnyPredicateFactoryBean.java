package org.springmodules.commons.collections.functors;

import java.util.Collection;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AnyPredicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>AnyPredicateFactoryBean returns an instance of
 * org.apache.commons.collections.functors.AnyPredicate.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.AnyPredicate
 */
public class AnyPredicateFactoryBean implements FactoryBean {

	private Collection predicates = null;
	
	public AnyPredicateFactoryBean() {
		super();
	}

	/**
	 * Sets the predicates collection.
	 * 
	 * @param predicates the predicates collection.
	 */
	public void setPredicates(Collection predicates) {
		this.predicates = predicates;
	}
	
	/**
	 * @return an instance of org.apache.commons.collections.functors.AnyPredicate.
	 * @see FactoryBean#getObject()
	 * @see AnyPredicate#getInstance(java.util.Collection)
	 */
	public Object getObject() throws Exception {
		if (predicates == null) {
			throw new IllegalArgumentException("[predicates] property has not been set!");
		}
		
		return AnyPredicate.getInstance(predicates);
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
