package org.springmodules.commons.collections.functors;

import java.util.Collection;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>ChainedTransformerFactoryBean returns an instance of
 * org.apache.commons.collections.functors.ChainedTransformer.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.ChainedTransformer
 */
public class ChainedTransformerFactoryBean implements FactoryBean {

	private Collection transformers = null;
	
	public ChainedTransformerFactoryBean() {
		super();
	}

	/**
	 * Sets the transformers to be chained.
	 * 
	 * @param transformers the collection of transformers to be chained
	 */
	public void setTransformers(Collection transformers) {
		this.transformers = transformers;
	}
	
	/**
	 * @return an instance of org.apache.commons.collections.functors.ChainedTransformer.
	 * @see FactoryBean#getObject()
	 * @see ChainedTransformer#getInstance(java.util.Collection)
	 */
	public Object getObject() throws Exception {
		if (transformers == null || transformers.isEmpty()) {
			throw new IllegalArgumentException("[transformer] property has not been set or is empty!");
		}
		return ChainedTransformer.getInstance(transformers);
	}

	/**
	 * @return org.apache.commons.collections.Transformer class
	 * @see FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Transformer.class;
	}

	/**
	 * @return true
	 * @see FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

}
