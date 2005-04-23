package org.springmodules.commons.collections.functors;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NotPredicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>NotPredicateFactoryBean returns an instance of
 * org.apache.commons.collections.functors.NotPredicate.
 * 
 * @author Steven Devijver
 * @since 23-05-2005
 * @see org.apache.commons.collections.functors.NotPredicate
 */
public class NotPredicateFactoryBean implements FactoryBean {

	private Predicate predicate = null;
	
	public NotPredicateFactoryBean() {
		super();
	}

	/**
	 * Sets the decorated predicate.
	 * 
	 * @param predicate the decorated predicate.
	 */
	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}
	
	/**
	 * @return an instance of org.apache.commons.collections.functors.NotPredicate.
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.NotPredicate#getInstance(org.apache.commons.collections.Predicate)
	 */
	public Object getObject() throws Exception {
		if (predicate == null) {
			throw new IllegalArgumentException("[predicate] property has not been set!");
		}
		
		return NotPredicate.getInstance(predicate);
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
