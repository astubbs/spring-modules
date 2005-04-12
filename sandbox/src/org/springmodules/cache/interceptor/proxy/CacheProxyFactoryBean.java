/* 
 * Created on Jan 17, 2005
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2005 the original author or authors.
 */

package org.springmodules.cache.interceptor.proxy;

import java.util.Properties;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.cache.interceptor.caching.CachingAttributeSourceAdvisor;
import org.springmodules.cache.interceptor.caching.CachingInterceptor;
import org.springmodules.cache.interceptor.caching.EntryStoredListener;
import org.springmodules.cache.interceptor.flush.CacheFlushAttributeSourceAdvisor;
import org.springmodules.cache.interceptor.flush.CacheFlushInterceptor;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * <p>
 * Proxy factory bean for simplified declarative caching services.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:55 $
 */
public final class CacheProxyFactoryBean extends ProxyConfig implements
    FactoryBean, InitializingBean {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3688501099833603120L;

  /**
   * Interceptor that performs cache flushing/invalidation when the advised
   * methods are executed.
   */
  private CacheFlushInterceptor cacheFlushInterceptor;

  /**
   * Interceptor that performs caching of the returned values of the advised
   * methods.
   */
  private CachingInterceptor cachingInterceptor;

  /**
   * The proxy to create.
   */
  private Object proxy;

  /**
   * Set of interfaces being proxied.
   */
  private Class[] proxyInterfaces;

  /**
   * The bean to be wrapped with a caching proxy.
   */
  private Object target;

  /**
   * Constructor.
   */
  public CacheProxyFactoryBean() {
    super();
    this.cacheFlushInterceptor = new CacheFlushInterceptor();
    this.cachingInterceptor = new CachingInterceptor();
  }

  /**
   * Creates the proxy for <code>{@link #target}</code>. This method is
   * invoked by a BeanFactory after it has set all bean properties supplied.
   * 
   * @throws IllegalStateException
   *           if the member variable 'target' is <code>null</code>.
   * @throws AopConfigException
   *           if the proxy interfaces are <code>null</code>, the flag
   *           'proxyTargetClass' is <code>false</code> and the target is an
   *           instance of <code>org.springframework.aop.TargetSource</code>.
   */
  public void afterPropertiesSet() {
    this.cachingInterceptor.afterPropertiesSet();

    if (this.target == null) {
      throw new IllegalStateException("Property 'target' is required");
    }

    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.addAdvisor(new CachingAttributeSourceAdvisor(
        this.cachingInterceptor));
    proxyFactory.addAdvisor(new CacheFlushAttributeSourceAdvisor(
        this.cacheFlushInterceptor));

    proxyFactory.copyFrom(this);

    TargetSource targetSource = this.createTargetSource(this.target);
    proxyFactory.setTargetSource(targetSource);

    if (this.proxyInterfaces != null) {
      proxyFactory.setInterfaces(this.proxyInterfaces);
    } else if (!super.isProxyTargetClass()) {
      if (this.target instanceof TargetSource) {
        throw new AopConfigException(
            "Either 'proxyInterfaces' or 'proxyTargetClass' is required "
                + "when using a TargetSource as 'target'");
      }

      // rely on AOP infrastructure to tell us what interfaces to proxy
      proxyFactory.setInterfaces(AopUtils.getAllInterfaces(this.target));
    }

    this.proxy = proxyFactory.getProxy();
  }

  /**
   * Set the target or <code>TargetSource</code>.
   * 
   * @param targetObject
   *          target. If this is an implementation of <code>TargetSource</code>
   *          it is used as our <code>TargetSource</code>; otherwise it is
   *          wrapped in a <code>SingletonTargetSource</code>.
   * @return a <code>TargetSource</code> for this object.
   */
  protected TargetSource createTargetSource(Object targetObject) {
    TargetSource targetSource = null;

    if (targetObject instanceof TargetSource) {
      targetSource = (TargetSource) targetObject;
    } else {
      targetSource = new SingletonTargetSource(targetObject);
    }

    return targetSource;
  }

  /**
   * Returns <code>{@link #proxy}</code>.
   * 
   * @return the proxy.
   */
  public Object getObject() {
    return this.proxy;
  }

  /**
   * <ul>
   * <li>If the <code>{@link #proxy}</code> is not <code>null</code>, it
   * returns the class the class of the <code>{@link #proxy}</code>.</li>
   * <li>If the <code>{@link #proxy}</code> is <code>null</code> and the
   * <code>{@link #target}</code> is not <code>null</code> and is an
   * instance of <code>TargetSource</code>, it will return the class of the
   * <code>{@link #target}</code>.</li>
   * <li>If both the <code>{@link #proxy}</code> and the
   * <code>{@link #target}</code> are <code>null</code>, it will return
   * <code>null</code>.</li>
   * </ul>
   */
  public Class getObjectType() {
    Class objectType = null;

    if (this.proxy != null) {
      objectType = this.proxy.getClass();
    } else if (this.target != null && this.target instanceof TargetSource) {
      objectType = this.target.getClass();
    }

    return objectType;
  }

  /**
   * Getter for <code>{@link #proxy}</code>.
   * 
   * @return the value of the member variable <code>proxy</code>.
   */
  protected Object getProxy() {
    return this.proxy;
  }

  /**
   * Getter for <code>{@link #proxyInterfaces}</code>.
   * 
   * @return the value of the member variable <code>proxyInterfaces</code>.
   */
  protected Class[] getProxyInterfaces() {
    return this.proxyInterfaces;
  }

  /**
   * This managed bean is a singleton.
   * 
   * @return <code>true</code>.
   */
  public boolean isSingleton() {
    return true;
  }

  /**
   * Set properties with method names as keys and caching-attribute descriptors
   * (parsed via
   * <code>{@link org.springmodules.cache.interceptor.flush.CacheFlushAttributeEditor}</code>)
   * as values.
   * <p>
   * Note: Method names are always applied to the target class, no matter if
   * defined in an interface or the class itself.
   * <p>
   * Internally, a
   * <code>{@link org.springmodules.cache.interceptor.flush.NameMatchCacheFlushAttributeSource}</code>
   * will be created from the given properties.
   * 
   * @see org.springmodules.cache.interceptor.flush.NameMatchCacheFlushAttributeSource
   */
  public void setCacheFlushAttributes(Properties cacheFlushAttributes) {
    this.cacheFlushInterceptor.setCacheFlushAttributes(cacheFlushAttributes);
  }

  /**
   * Sets the cache provider facade for the interceptors
   * <code>{@link #cacheFlushInterceptor}</code> and
   * <code>{@link #cachingInterceptor}</code>.
   * 
   * @param cacheProviderFacade
   *          the cache provider facade.
   */
  public void setCacheProviderFacade(CacheProviderFacade cacheProviderFacade) {
    this.cacheFlushInterceptor.setCacheProviderFacade(cacheProviderFacade);
    this.cachingInterceptor.setCacheProviderFacade(cacheProviderFacade);
  }

  /**
   * Set properties with method names as keys and caching-attribute descriptors
   * (parsed via
   * <code>{@link org.springmodules.cache.interceptor.caching.CachingAttributeEditor}</code>)
   * as values.
   * <p>
   * Note: Method names are always applied to the target class, no matter if
   * defined in an interface or the class itself.
   * <p>
   * Internally, a
   * <code>{@link org.springmodules.cache.interceptor.caching.NameMatchCachingAttributeSource}</code>
   * will be created from the given properties.
   * 
   * @see org.springmodules.cache.interceptor.caching.NameMatchCachingAttributeSource
   */
  public void setCachingAttributes(Properties cachingAttributes) {
    this.cachingInterceptor.setCachingAttributes(cachingAttributes);
  }

  /**
   * Sets the listener to be notified each time an entry is stored in the cache.
   * 
   * @param entryStoredListener
   *          the listener.
   */
  public void setEntryStoredListener(EntryStoredListener entryStoredListener) {
    this.cachingInterceptor.setEntryStoredListener(entryStoredListener);
  }

  /**
   * <p>
   * Specify the set of interfaces being proxied.
   * </p>
   * <p>
   * If left null (the default), the AOP infrastructure works out which
   * interfaces need proxying by analyzing the target, proxying all the
   * interfaces that the target object implements.
   * </p>
   * 
   * @throws ClassNotFoundException
   *           if any of the classes can't be loaded
   */
  public void setProxyInterfaces(String[] interfaceNames)
      throws ClassNotFoundException {
    this.proxyInterfaces = AopUtils.toInterfaceArray(interfaceNames);
  }

  /**
   * Setter for the field <code>{@link #target}</code>.
   * 
   * @param target
   *          the new value to set
   */
  public final void setTarget(Object target) {
    this.target = target;
  }

}