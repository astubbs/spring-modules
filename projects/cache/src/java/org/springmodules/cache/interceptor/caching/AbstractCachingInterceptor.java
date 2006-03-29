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

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.key.CacheKeyGenerator;
import org.springmodules.cache.key.HashCodeCacheKeyGenerator;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * <p>
 * Template for advices that store in a cache the return value of intercepted
 * methods.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCachingInterceptor implements MethodInterceptor,
    InitializingBean {

  public static class NullObject implements Serializable {

    private static final long serialVersionUID = 3257007674280522803L;

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof NullObject)) {
        return false;
      }

      return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
      return 80992;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
      String identity = ObjectUtils.getIdentityHexString(this);
      return getClass().getName() + "@" + identity;
    }
  }

  /**
   * Canonical value held in cache to indicate that the return value of the
   * method to apply caching to is <code>null</code>.
   */
  public static final NullObject NULL_ENTRY = new NullObject();

  /** Logger available to subclasses */
  protected final Log logger = LogFactory.getLog(getClass());

  private CacheKeyGenerator cacheKeyGenerator;

  private CacheProviderFacade cacheProviderFacade;

  private CachingListener[] cachingListeners;

  /**
   * Map of <code>{@link CachingModel}</code>s that specify how to store,
   * retrieve and remove objects from the cache. Each cache model is stored
   * under a unique id (a String).
   */
  private Map cachingModels;

  /**
   * Performs property validation and initialization after this interceptor has
   * been created and configured.
   * <ul>
   * <li>Verifies that caching models have been set.</li>
   * <li>Validates that the caching models have valid property values</li>
   * <li>Eagerly initialize the key generator, creating a new
   * <code>{@link HashCodeCacheKeyGenerator}</code> if none set.
   * </ul>
   * 
   * @throws FatalCacheException
   *           if the cache provider facade is <code>null</code>
   * @throws FatalCacheException
   *           if the map of caching models is <code>null</code> or empty
   * @throws FatalCacheException
   *           if an unexpected exception is thrown when creating caching models
   *           from <code>java.util.Properties</code>
   * @throws FatalCacheException
   *           if one or more caching models have invalid property values
   * 
   * @see InitializingBean#afterPropertiesSet()
   * @see #onAfterPropertiesSet()
   */
  public final void afterPropertiesSet() throws FatalCacheException {
    if (cacheProviderFacade == null) {
      throw new FatalCacheException(
          "The cache provider facade should not be null");
    }

    if (cachingModels == null || cachingModels.isEmpty()) {
      throw new FatalCacheException(
          "The map of caching models should not be empty");
    }

    if (cachingModels instanceof Properties) {
      setCachingModelsFromProperties();

    } else {
      validateCachingModels();
    }

    if (cacheKeyGenerator == null) {
      cacheKeyGenerator = new HashCodeCacheKeyGenerator(true);
      setCacheKeyGenerator(cacheKeyGenerator);
    }

    onAfterPropertiesSet();
  }

  /**
   * @return the generator of cache entry keys.
   */
  public final CacheKeyGenerator getCacheKeyGenerator() {
    return cacheKeyGenerator;
  }

  /**
   * <p>
   * Performs caching following these steps:
   * <ol>
   * <li>Generates a unique key from the description of the invocation to the
   * intercepted method</li>
   * <li>Verifies if the cache already has an entry under the generated key, if
   * it does returns the cached value</li>
   * <li>Otherwise executes the intercepted method, stores its return value in
   * the cache under the generated key and returns the new cached value</li>
   * </ol>
   * </p>
   * <p>
   * Intercepted methods which return type is <code>void</code> are ignored.
   * </p>
   * 
   * @param methodInvocation
   *          the description of the intercepted method
   * @return the object stored in the cache
   * @throws Throwable
   *           any exception thrown when executing the intercepted method
   */
  public final Object invoke(MethodInvocation methodInvocation)
      throws Throwable {
    Method method = methodInvocation.getMethod();
    if (!CachingUtils.isCacheable(method)) {
      logger.debug("Unable to perform caching. Intercepted method <"
          + method.getName() + "> does not return a value");
      return methodInvocation.proceed();
    }

    CachingModel model = getModel(methodInvocation);

    if (model == null) {
      logger.debug("Unable to perform caching. "
          + "No model is associated to the intercepted method <"
          + method.getName() + ">");
      return methodInvocation.proceed();
    }

    Serializable key = cacheKeyGenerator.generateKey(methodInvocation);

    Object cachedObject = cacheProviderFacade.getFromCache(key, model);

    if (null == cachedObject) {
      Throwable exceptionThrownByProceed = null;

      try {
        cachedObject = methodInvocation.proceed();

      } catch (Throwable throwable) {
        logger.error("Unable to proceed to the next interceptor in the chain",
            throwable);
        exceptionThrownByProceed = throwable;
      }

      if (null == exceptionThrownByProceed) {
        if (null == cachedObject) {
          cacheProviderFacade.putInCache(key, model, NULL_ENTRY);
        } else {
          cacheProviderFacade.putInCache(key, model, cachedObject);
        }

        // notify the listeners a new entry was stored in the cache.
        if (!ObjectUtils.isEmpty(cachingListeners)) {
          int listenerCount = cachingListeners.length;
          for (int i = 0; i < listenerCount; i++) {
            cachingListeners[i].onCaching(key, cachedObject, model);
          }
        }
      } else {
        cacheProviderFacade.cancelCacheUpdate(key);
        throw exceptionThrownByProceed;
      }

    } else if (NULL_ENTRY == cachedObject) {
      cachedObject = null;
    }

    return cachedObject;
  }

  /**
   * Sets the generator of cache entry keys.
   * 
   * @param newCacheKeyGenerator
   *          the new generator of cache entry keys
   */
  public final void setCacheKeyGenerator(CacheKeyGenerator newCacheKeyGenerator) {
    cacheKeyGenerator = newCacheKeyGenerator;
  }

  /**
   * Sets the facade for the cache provider to use.
   * 
   * @param newCacheProviderFacade
   *          the new cache provider facade
   */
  public final void setCacheProviderFacade(
      CacheProviderFacade newCacheProviderFacade) {
    cacheProviderFacade = newCacheProviderFacade;
  }

  /**
   * Sets the listeners to be notified when an object is stored in the cache.
   * 
   * @param newCachingListeners
   *          the new listeners
   */
  public final void setCachingListeners(CachingListener[] newCachingListeners) {
    cachingListeners = newCachingListeners;
  }

  /**
   * Sets the caching models to use.
   * 
   * @param newCachingModels
   *          the new caching models
   */
  public final void setCachingModels(Map newCachingModels) {
    cachingModels = newCachingModels;
  }

  /**
   * @return the map that specifies how caching models should be bound to class
   *         methods
   */
  protected final Map getCachingModels() {
    return cachingModels;
  }

  /**
   * Returns the caching model bound to an advised method.
   * 
   * @param methodInvocation
   *          the description of an invocation to the advised method
   * @return the caching model bound to an advised method
   */
  protected abstract CachingModel getModel(MethodInvocation methodInvocation);

  /**
   * Gives subclasses the opportunity to initialize and validate their own
   * properties.
   * 
   * @throws FatalCacheException
   *           if one or more properties of this interceptor contain invalid
   *           values or have an illegal state
   */
  protected void onAfterPropertiesSet() throws FatalCacheException {
    // no implementation.
  }

  /**
   * <p>
   * <ol>
   * <li>Creates a new caching model from each property using a
   * <code>PropertyEditor</code></li>
   * <li>Validates the created caching model using a
   * <code>{@link CacheModelValidator}</code></li>
   * <li>Stores the created caching model in a <code>java.util.Map</code>
   * using the same key as the property previously used</li>
   * <li>Replaces the <code>java.util.Properties</code> with the created map</li>
   * </ol>
   * </p>
   * <p>
   * <b>Note:</b> This method is executed by
   * <code>{@link #afterPropertiesSet()}</code> <b>only</b> when
   * <code>{@link #cachingModels}</code> has been set as a
   * <code>java.util.Properties</code>.
   * </p>
   * 
   * @throws FatalCacheException
   *           if an unexpected exception is thrown when creating or validating
   *           a caching model
   * @see CacheModelValidator#validateCachingModel(Object)
   */
  private void setCachingModelsFromProperties() throws FatalCacheException {
    CacheModelValidator validator = cacheProviderFacade
        .getCacheModelValidator();

    PropertyEditor editor = cacheProviderFacade.getCachingModelEditor();
    Properties properties = (Properties) cachingModels;
    Map newCachingModels = new HashMap();

    String id = null;

    try {
      for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
        id = (String) i.next();

        String property = properties.getProperty(id);
        editor.setAsText(property);
        Object cachingModel = editor.getValue();
        validator.validateCachingModel(cachingModel);

        newCachingModels.put(id, cachingModel);
      }
    } catch (Exception exception) {
      throw new FatalCacheException(
          "Unable to create the caching model with id " + StringUtils.quote(id),
          exception);
    }

    setCachingModels(newCachingModels);
  }

  /**
   * <p>
   * Validates the caching models in <code>{@link #cachingModels}</code>.
   * </p>
   * <p>
   * <b>Note:</b> This method is executed by
   * <code>{@link #afterPropertiesSet()}</code> <b>only</b> when
   * <code>{@link #cachingModels}</code> has <b>not</b> been set as a
   * <code>java.util.Properties</code>.
   * </p>
   * 
   * @throws FatalCacheException
   *           wrapping any exception thrown by the validator
   */
  private void validateCachingModels() throws FatalCacheException {
    CacheModelValidator validator = cacheProviderFacade
        .getCacheModelValidator();

    String id = null;

    try {
      for (Iterator i = cachingModels.keySet().iterator(); i.hasNext();) {
        id = (String) i.next();
        Object cachingModel = cachingModels.get(id);
        validator.validateCachingModel(cachingModel);
      }
    } catch (Exception exception) {
      throw new FatalCacheException("Unable to validate caching model with id "
          + StringUtils.quote(id), exception);
    }
  }
}