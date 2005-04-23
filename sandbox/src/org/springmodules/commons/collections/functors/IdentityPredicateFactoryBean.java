package org.springmodules.commons.collections.functors;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.IdentityPredicate;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>IdentityPredicateFactoryBean returns an instance of
 * org.apache.commons.collections.functors.IdentityPredicate.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.IdentityPredicate
 */
public class IdentityPredicateFactoryBean implements FactoryBean {

	private Object identity = null;
	
	public IdentityPredicateFactoryBean() {
		super();
	}

	/**
	 * Sets is the identity.
	 * 
	 * @param identity the identity.
	 */
	public void setIdentity(Object identity) {
		this.identity = identity;
	}
	
	/**
	 * @return an instance of org.apache.commons.collections.functors.IdentityPredicate.
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.IdentityPredicate#getInstance(java.lang.Object)
	 */
	public Object getObject() throws Exception {
		if (identity == null) {
			throw new IllegalArgumentException("[identity] property has not been set!");
		}
		
		return IdentityPredicate.getInstance(identity);
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
