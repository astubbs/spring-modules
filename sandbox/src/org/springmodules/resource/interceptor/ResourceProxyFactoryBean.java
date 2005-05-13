/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.resource.interceptor;

import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springmodules.resource.ResourceManager;

/**
 * Proxy factory bean for simplified declarative transaction handling.
 * Alternative to the standard AOP ProxyFactoryBean with a TransactionInterceptor.
 *
 * <p>This class is intended to cover the <i>typical</i> case of declarative
 * transaction demarcation: namely, wrapping a singleton target object with a
 * transactional proxy, proxying all the interfaces that the target implements.
 *
 * <p>There are three main properties to be specified:
 *
 * <ul>
 * <li>"transactionManager": the PlatformTransactionManager implementation to use
 * (for example, a JtaTransactionManager instance)
 * <li>"target": the target object that a transactional proxy shouls be created for
 * <li>"transactionAttributes": the transaction attributes (for example, propagation
 * behavior and "readOnly" flag) per target method name (or method name pattern)
 * </ul>
 *
 * <p>If the "transactionManager" property is not set explicitly and this FactoryBean
 * is running in a ListableBeanFactory, a single matching bean of type
 * PlatformTransactionManager will be fetched from the BeanFactory.
 *
 * <p>In contrast to TransactionInterceptor, the transaction attributes are
 * specified as properties, with method names as keys and transaction attribute
 * descriptors as values. Method names are always applied to the target class.
 *
 * <p>Internally, a TransactionInterceptor instance is used, but the user of this
 * class does not have to care. Optionally, a MethodPointcut can be specified
 * to cause conditional invocation of the underlying TransactionInterceptor.
 *
 * <p>The "preInterceptors" and "postInterceptors" properties can be set to add
 * additional interceptors to the mix, like PerformanceMonitorInterceptor or
 * HibernateInterceptor/JdoInterceptor.
 *
 * @author Juergen Hoeller
 * @author Dmitriy Kopylenko
 * @author Rod Johnson
 * @since 21.08.2003
 * @see #setTransactionManager
 * @see #setTarget
 * @see #setTransactionAttributes
 * @see TransactionInterceptor
 * @see org.springframework.aop.framework.ProxyFactoryBean
 */
public class ResourceProxyFactoryBean extends ProxyConfig
		implements FactoryBean, BeanFactoryAware, InitializingBean {

	private final ResourceInterceptor resourceInterceptor = new ResourceInterceptor();

	private Object target;

	private Class[] proxyInterfaces;

	private Pointcut pointcut;

	private Object[] preInterceptors;

	private Object[] postInterceptors;

	/** Default is global AdvisorAdapterRegistry */
	private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();

	private Object proxy;


	/**
	 * Set the resource manager. This will perform actual
	 * resource management: This class is just a way of invoking it.
	 * @see ResourceInterceptor#setresourceManager
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceInterceptor.setResourceManager(resourceManager);
	}

	/**
	 * Set the target object, i.e. the bean to be wrapped with a resource proxy.
	 * <p>The target may be any object, in which case a SingletonTargetSource will
	 * be created. If it is a TargetSource, no wrapper TargetSource is created:
	 * This enables the use of a pooling or prototype TargetSource etc.
	 * @see org.springframework.aop.TargetSource
	 * @see org.springframework.aop.target.SingletonTargetSource
	 * @see org.springframework.aop.target.LazyInitTargetSource
	 * @see org.springframework.aop.target.PrototypeTargetSource
	 * @see org.springframework.aop.target.CommonsPoolTargetSource
	 */
	public void setTarget(Object target) {
		this.target = target;
	}

	/**
	 * Specify the set of interfaces being proxied.
	 * <p>If left null (the default), the AOP infrastructure works
	 * out which interfaces need proxying by analyzing the target,
	 * proxying all the interfaces that the target object implements.
	 */
	public void setProxyInterfaces(String[] interfaceNames) throws ClassNotFoundException {
		this.proxyInterfaces = AopUtils.toInterfaceArray(interfaceNames);
	}

	/**
	 * Set a pointcut, i.e a bean that can cause conditional invocation
	 * of the TransactionInterceptor depending on method and attributes passed.
	 * Note: Additional interceptors are always invoked.
	 * @see #setPreInterceptors
	 * @see #setPostInterceptors
	 */
	public void setPointcut(Pointcut pointcut) {
		this.pointcut = pointcut;
	}

	/**
	 * Set additional interceptors (or advisors) to be applied before the
	 * implicit transaction interceptor, e.g. PerformanceMonitorInterceptor.
	 * @see org.springframework.aop.interceptor.PerformanceMonitorInterceptor
	 */
	public void setPreInterceptors(Object[] preInterceptors) {
		this.preInterceptors = preInterceptors;
	}

	/**
	 * Set additional interceptors (or advisors) to be applied after the
	 * implicit transaction interceptor, e.g. HibernateInterceptors for
	 * eagerly binding Sessions to the current thread when using JTA.
	 * <p>Note that this is just necessary if you rely on those interceptors in general:
	 * HibernateTemplate and JdoTemplate work nicely with JtaTransactionManager through
	 * implicit on-demand thread binding.
	 * @see org.springframework.orm.hibernate.HibernateInterceptor
	 * @see org.springframework.orm.jdo.JdoInterceptor
	 */
	public void setPostInterceptors(Object[] postInterceptors) {
		this.postInterceptors = postInterceptors;
	}

	/**
	 * Specify the AdvisorAdapterRegistry to use.
	 * Default is the global AdvisorAdapterRegistry.
	 * @see org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry
	 */
	public void setAdvisorAdapterRegistry(AdvisorAdapterRegistry advisorAdapterRegistry) {
		this.advisorAdapterRegistry = advisorAdapterRegistry;
	}

	/**
	 * This callback is optional: If running in a BeanFactory and no transaction
	 * manager has been set explicitly, a single matching bean of type
	 * PlatformTransactionManager will be fetched from the BeanFactory.
	 * @see org.springframework.beans.factory.BeanFactoryUtils#beanOfTypeIncludingAncestors
	 * @see org.springframework.transaction.PlatformTransactionManager
	 */
	public void setBeanFactory(BeanFactory beanFactory) {
		if (this.resourceInterceptor.getResourceManager() == null &&
				beanFactory instanceof ListableBeanFactory) {
			ListableBeanFactory lbf = (ListableBeanFactory) beanFactory;
			ResourceManager rm = (ResourceManager)
					BeanFactoryUtils.beanOfTypeIncludingAncestors(lbf, ResourceManager.class);
			this.resourceInterceptor.setResourceManager(rm);
		}
	}


	public void afterPropertiesSet() {
		this.resourceInterceptor.afterPropertiesSet();

		if (this.target == null) {
			throw new IllegalArgumentException("'target' is required");
		}
		
		ProxyFactory proxyFactory = new ProxyFactory();

		if (this.preInterceptors != null) {
			for (int i = 0; i < this.preInterceptors.length; i++) {
				proxyFactory.addAdvisor(this.advisorAdapterRegistry.wrap(this.preInterceptors[i]));
			}
		}

		if (this.pointcut != null) {
			Advisor advice = new DefaultPointcutAdvisor(this.pointcut, this.resourceInterceptor);
			proxyFactory.addAdvisor(advice);
		}
		else {
			// rely on default pointcut
			proxyFactory.addAdvisor(new ResourceAdvisor(this.resourceInterceptor));
			// Could just do the following, but it's usually less efficient because of AOP advice chain caching.
			// proxyFactory.addInterceptor(transactionInterceptor);
		}

		if (this.postInterceptors != null) {
			for (int i = 0; i < this.postInterceptors.length; i++) {
				proxyFactory.addAdvisor(this.advisorAdapterRegistry.wrap(this.postInterceptors[i]));
			}
		}

		proxyFactory.copyFrom(this);

		TargetSource targetSource = createTargetSource(this.target);
		proxyFactory.setTargetSource(targetSource);

		if (this.proxyInterfaces != null) {
			proxyFactory.setInterfaces(this.proxyInterfaces);
		}
		else if (!isProxyTargetClass()) {
			// Rely on AOP infrastructure to tell us what interfaces to proxy.
			proxyFactory.setInterfaces(AopUtils.getAllInterfacesForClass(targetSource.getTargetClass()));
		}
		
		this.proxy = proxyFactory.getProxy();
	}

	/**
	 * Set the target or TargetSource.
	 * @param target target. If this is an implementation of TargetSource it is
	 * used as our TargetSource; otherwise it is wrapped in a SingletonTargetSource.
	 * @return a TargetSource for this object
	 */
	protected TargetSource createTargetSource(Object target) {
		if (target instanceof TargetSource) {
			return (TargetSource) target;
		}
		else {
			return new SingletonTargetSource(target);
		}
	}

	public Object getObject() {
		return this.proxy;
	}

	public Class getObjectType() {
		if (this.proxy != null) {
			return this.proxy.getClass();
		}
		else if (this.proxyInterfaces != null && this.proxyInterfaces.length == 1) {
			return this.proxyInterfaces[0];
		}
		else if (this.target instanceof TargetSource) {
			return ((TargetSource) this.target).getTargetClass();
		}
		else if (this.target != null) {
			return this.target.getClass();
		}
		else {
			return null;
		}
	}

	public boolean isSingleton() {
		return true;
	}

}
