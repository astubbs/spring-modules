package org.springmodules.commons.collections.functors;

import java.util.Collection;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.functors.ChainedClosure;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>ChainedClosureFactoryBean returns an instance of
 * org.apache.commons.collections.functors.ChainedClosure.
 * 
 * @author Steven Devijver
 * @since 28-04-2005
 * @see org.apache.commons.collections.functors.ChainedClosure
 */
public class ChainedClosureFactoryBean implements FactoryBean, InitializingBean {

	private Closure closure = null;
	private Collection closures = null;
	
	public ChainedClosureFactoryBean() {
		super();
	}

	/**
	 * <p>The closures to chain (required).
	 * 
	 * @param closures the closures to chain
	 */
	public void setClosures(Collection closures) {
		this.closures = closures;
	}
	
	private Collection getClosures() {
		if (this.closures == null || this.closures.isEmpty()) {
			throw new IllegalArgumentException("[closures] property is not set!");
		}
		return this.closures;
	}
	
	public Object getObject() throws Exception {
		return this.closure;
	}

	public Class getObjectType() {
		return Closure.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		this.closure = ChainedClosure.getInstance(getClosures());
	}

}
