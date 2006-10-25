package org.springmodules.ant.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Utility class to load a (possibly hierarchical) BeanFactory in the context
 * of an Ant task or type.
 * 
 * @author Dave Syer
 *
 */
public abstract class BeanFactoryLoader {
	
	/**
	 * The default location of a resource (or set of resources) containing BeanFactory
	 * bean definitions.
	 */
	public static String DEFAULT_CONTEXT_REF = "classpath*:beanRefContext.xml";
	
	private static Map factoryCache = new HashMap();

	/**
	 * Load a BeanFactory from the default context and given factoryKey.
	 * @param factoryKey bean name idenitifying a BeanFactory instance in the contextRef.
	 * @return BeanFactory instance if it exists, null if not.
	 * @throws BuildException if the BeanFactory cannot be initialized.
	 */
	public static BeanFactory getBeanFactory(String factoryKey) throws BuildException {
		return getBeanFactory(DEFAULT_CONTEXT_REF, factoryKey);
	}
		
	/**
	 * Load a BeanFactory from the given context and factoryKey.
	 * @param contextRef location of context bean definitions (e.g. classpath*:beanRefContext.xml).
	 * @param factoryKey bean name idenitifying a BeanFactory instance in the contextRef.
	 * @return BeanFactory instance if it exists, null if not.
	 * @throws BuildException if the BeanFactory cannot be initialized.
	 */
	public static BeanFactory getBeanFactory(String contextRef, String factoryKey) throws BuildException {

		String cacheKey = contextRef + factoryKey;
		BeanFactory beanFactory = (BeanFactory)factoryCache.get(cacheKey);
		
		// Try to cache factories as we load them...
		if (beanFactory!=null) return beanFactory;

		try {
			beanFactory = SingletonBeanFactoryLocator.getInstance(
					contextRef).useBeanFactory(factoryKey)
					.getFactory();
			refresh(beanFactory);
		} catch (BeansException e) {
			throw new BuildException("Cannot locate the bean factory: ["
					+ contextRef + "].[" + factoryKey + "]", e);
		}
		
		factoryCache.put(cacheKey, beanFactory);
		return beanFactory;
	}
	
	private static void refresh(BeanFactory beanFactory) {
		// Refresh the parent first (recursively)...
		if (beanFactory instanceof HierarchicalBeanFactory) {
			refresh(((HierarchicalBeanFactory)beanFactory).getParentBeanFactory());
		}
		if (beanFactory instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext)beanFactory).refresh();
		}
	}

}
