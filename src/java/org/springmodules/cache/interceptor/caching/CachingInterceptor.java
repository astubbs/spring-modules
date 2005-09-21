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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.cache.key.CacheKeyGenerator;
import org.springmodules.cache.key.HashCodeCacheKeyGenerator;
import org.springmodules.cache.provider.CacheProviderFacade;
import org.springmodules.cache.provider.CacheProviderFacadeStatus;

/**
 * <p>
 * Caches the return value of the intercepted method.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.9 $ $Date: 2005/09/21 02:45:54 $
 */
public class CachingInterceptor extends CachingAspectSupport implements
    MethodInterceptor, InitializingBean {

  private static Log logger = LogFactory.getLog(CachingInterceptor.class);

  private CacheProviderFacade cacheProviderFacade;

  public CachingInterceptor() {
    super();
  }

  /**
   * @see InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() {
    CacheKeyGenerator cacheKeyGenerator = getCacheKeyGenerator();

    if (cacheKeyGenerator == null) {
      cacheKeyGenerator = new HashCodeCacheKeyGenerator(true);
      setCacheKeyGenerator(cacheKeyGenerator);
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

    CachingAttributeSource attributeSource = getCachingAttributeSource();
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

    if (cacheProviderFacade.getStatus() != CacheProviderFacadeStatus.READY) {
      logger.info(cacheProviderFacade.getStatus());
      return methodInvocation.proceed();
    }

    Cached cachingAttribute = getCachingAttribute(methodInvocation);

    if (cachingAttribute == null) {
      // no caching performed, we don't have any information about the cache
      return methodInvocation.proceed();
    }

    String cacheProfileId = cachingAttribute.getCacheProfileId();

    CacheKeyGenerator cacheKeyGenerator = getCacheKeyGenerator();
    Serializable cacheKey = cacheKeyGenerator.generateKey(methodInvocation);

    Object cachedObject = cacheProviderFacade.getFromCache(cacheKey,
        cacheProfileId);

    if (null == cachedObject) {
      Throwable exceptionThrownByProceed = null;

      try {
        cachedObject = methodInvocation.proceed();

      } catch (Throwable throwable) {
        if (logger.isErrorEnabled()) {
          String message = "Unable to proceed to the next interceptor in the chain";
          logger.error(message, throwable);
        }
        exceptionThrownByProceed = throwable;
      }

      if (null == exceptionThrownByProceed) {
        if (null == cachedObject) {
          cacheProviderFacade.putInCache(cacheKey, cacheProfileId,
              CachingAspectSupport.NULL_ENTRY);
        } else {
          cacheProviderFacade
              .putInCache(cacheKey, cacheProfileId, cachedObject);
        }

        // notify the listener a new entry was stored in the cache.
        EntryStoredListener listener = getEntryStoredListener();
        if (listener != null) {
          listener.onEntryAdd(cacheKey, cachedObject);
        }
      } else {
        cacheProviderFacade.cancelCacheUpdate(cacheKey);
        throw exceptionThrownByProceed;
      }

    } else if (CachingAspectSupport.NULL_ENTRY == cachedObject) {
      cachedObject = null;
    }

    return cachedObject;
  }

  public final void setCacheProviderFacade(
      CacheProviderFacade newCacheProviderFacade) {
    cacheProviderFacade = newCacheProviderFacade;
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
    setCachingAttributeSource(attributeSource);
  }
}