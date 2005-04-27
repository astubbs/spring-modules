/* 
 * Created on Nov 10, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;
import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.cache.EntryRetrievalException;
import org.springmodules.cache.key.CacheKey;
import org.springmodules.cache.key.CacheKeyGenerator;
import org.springmodules.cache.key.HashCodeCacheKeyGenerator;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * <p>
 * Caches the return value of the intercepted method.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/04/27 01:41:09 $
 */
public class CachingInterceptor extends CachingAspectSupport implements
    MethodInterceptor, InitializingBean {

  /**
   * Message logger.
   */
  private static Log logger = LogFactory.getLog(CachingInterceptor.class);

  /**
   * Cache provider facade.
   */
  private CacheProviderFacade cacheProviderFacade;

  /**
   * Constructor.
   */
  public CachingInterceptor() {

    super();
  }

  /**
   * Event raised by the Spring container indicating that all the properties of
   * this class has been set. If the key generator is <code>null</code>, this
   * method will create a new instance of
   * <code>{@link HashCodeCacheKeyGenerator}</code>.
   */
  public void afterPropertiesSet() {
    CacheKeyGenerator cacheKeyGenerator = super.getCacheKeyGenerator();

    if (cacheKeyGenerator == null) {
      cacheKeyGenerator = new HashCodeCacheKeyGenerator(true);
      super.setCacheKeyGenerator(cacheKeyGenerator);
    }
  }

  /**
   * Returns the metadata attribute of the intercepted method.
   * 
   * @param methodInvocation
   *          the description of an invocation to the method.
   * @return the metadata attribute of the intercepted method.
   */
  protected final Cached getCachingAttribute(MethodInvocation methodInvocation) {
    Method method = methodInvocation.getMethod();

    Object thisObject = methodInvocation.getThis();
    Class targetClass = (thisObject != null) ? thisObject.getClass() : null;

    CachingAttributeSource attributeSource = super.getCachingAttributeSource();
    Cached attribute = attributeSource.getCachingAttribute(method, targetClass);

    return attribute;
  }

  /**
   * Returns from the cache provider the value saved with the key generated
   * using the specified <code>MethodInvocation</code>. If the object is not
   * found in the cache, the intercepted method is executed and its returned
   * value is saved in the cached and returned by this method.
   * 
   * @param methodInvocation
   *          the description of the intercepted method.
   * @return the object stored in the cache.
   */
  public final Object invoke(MethodInvocation methodInvocation)
      throws Throwable {

    Object cachedObject = null;

    Cached cachingAttribute = this.getCachingAttribute(methodInvocation);

    if (cachingAttribute == null) {
      // no caching performed, we don't have any information about the cache
      return methodInvocation.proceed();
    }

    String cacheProfileId = cachingAttribute.getCacheProfileId();

    CacheKeyGenerator cacheKeyGenerator = super.getCacheKeyGenerator();
    CacheKey cacheKey = cacheKeyGenerator.generateKey(methodInvocation);

    try {
      cachedObject = this.cacheProviderFacade.getFromCache(cacheKey,
          cacheProfileId);
    } catch (EntryRetrievalException entryRetrievalException) {
      if (logger.isErrorEnabled()) {
        String message = "Exception thrown when retrieving an entry from the cache";
        logger.error(message, entryRetrievalException);
      }

      // we couldn't get the entry from the cache.
      return methodInvocation.proceed();
    }

    if (null == cachedObject) {
      logger.debug("Method 'invoke(..)'. Object not found in the cache");

      Throwable exceptionThrowByProceed = null;

      try {
        cachedObject = methodInvocation.proceed();
      } catch (Throwable throwable) {
        if (logger.isErrorEnabled()) {
          String message = "Exception thrown when executing 'MethodInvocation.proceed()'";
          logger.error(message, throwable);
        }
        exceptionThrowByProceed = throwable;
      }

      if (null == exceptionThrowByProceed) {
        if (null == cachedObject) {
          this.cacheProviderFacade.putInCache(cacheKey, cacheProfileId,
              NULL_ENTRY);
        } else {
          this.cacheProviderFacade.putInCache(cacheKey, cacheProfileId,
              cachedObject);
        }

        // notify the listener a new entry was stored in the cache.
        EntryStoredListener listener = super.getEntryStoredListener();
        if (listener != null) {
          listener.onEntryAdd(cacheKey, cachedObject);
        }
      } else {
        this.cacheProviderFacade.cancelCacheUpdate(cacheKey);
        throw exceptionThrowByProceed;
      }
    } else if (NULL_ENTRY == cachedObject) {
      cachedObject = null;
    }

    return cachedObject;
  }

  /**
   * Setter for the field <code>{@link #cacheProviderFacade}</code>.
   * 
   * @param cacheProviderFacade
   *          the new value to set
   */
  public final void setCacheProviderFacade(
      CacheProviderFacade cacheProviderFacade) {
    this.cacheProviderFacade = cacheProviderFacade;
  }

  /**
   * Set properties with method names as keys and caching-attribute descriptors
   * (parsed via <code>{@link CachingAttributeEditor}</code>) as values.
   * <p>
   * Note: Method names are always applied to the target class, no matter if
   * defined in an interface or the class itself.
   * <p>
   * Internally, a <code>{@link NameMatchCachingAttributeSource}</code> will
   * be created from the given properties.
   * 
   * @see NameMatchCachingAttributeSource
   */
  public final void setCachingAttributes(Properties cachingAttributes) {
    NameMatchCachingAttributeSource attributeSource = new NameMatchCachingAttributeSource();
    attributeSource.setProperties(cachingAttributes);
    super.setCachingAttributeSource(attributeSource);
  }
}