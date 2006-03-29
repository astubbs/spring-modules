package org.springmodules.commons.collections.functors;

import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.MapTransformer;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>MapTransformerFactoryBean returns an instance of
 * org.apache.commons.collections.functors.MapTransformer.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 * @see org.apache.commons.collections.functors.MapTransformer
 */
public class MapTransformerFactoryBean implements FactoryBean {

	private Map map = null;
	
	public MapTransformerFactoryBean() {
		super();
	}

	/**
	 * Sets the lookup map.
	 *  
	 * @param map the lookup map 
	 */
	public void setMap(Map map) {
		this.map = map;
	}
	
	/**
	 * @return an instance of org.apache.commons.collections.functors.MapTransformer.
	 * @see FactoryBean#getObject()
	 * @see MapTransformer#getInstance(java.util.Map)
	 */
	public Object getObject() throws Exception {
		return MapTransformer.getInstance(map);
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
