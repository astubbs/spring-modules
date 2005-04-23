package org.springmodules.commons.collections.functors;

import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>SwitchTransformerFactoryBean returns an instanceof of
 * org.apache.commons.collections.functors.SwitchTransformer.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.SwitchTransformer
 */
public class SwitchTransformerFactoryBean implements FactoryBean {

	private Map predicatesAndTransformers = null;
	
	public SwitchTransformerFactoryBean() {
		super();
	}

	/**
	 * Sets the predicates and tranformers map.
	 * 
	 * @param predicatesAndTransformers the predicates and transformers map.
	 */
	public void setPredicatesAndTransformers(Map predicatesAndTransformers) {
		this.predicatesAndTransformers = predicatesAndTransformers;
	}
	
	/**
	 * @return an instance of org.apache.commons.collections.functors.SwitchTransformer.
	 * @see FactoryBean#getObject()
	 * @see org.apache.commons.collections.functors.SwitchTransformer#getInstance(java.util.Map)
	 */
	public Object getObject() throws Exception {
		return null;
	}

	/**
	 * @return org.apache.commons.collections.Transformer class.
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
