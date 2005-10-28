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
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.key.CacheKeyGenerator;
import org.springmodules.cache.key.HashCodeCacheKeyGenerator;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.CacheProviderFacade;
import org.springmodules.util.ArrayUtils;
import org.springmodules.util.Strings;

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

  /**
   * Canonical value held in cache to indicate that the return value of the
   * method to apply caching to is <code>null</code>.
   */
  public static final Serializable NULL_ENTRY = new Serializable() {

    private static final long serialVersionUID = 3257007674280522803L;

    public String toString() {
      return "NULL_ENTRY@" + System.identityHashCode(this);
    }
  };

  /**
   * Generates the keys for cache entries.
   */
  private CacheKeyGenerator cacheKeyGenerator;

  private CacheProviderFacade cacheProviderFacade;

  /**
   * Listener(s) being notified each time an entry is stored in the cache.
   */
  private CachingListener[] cachingListeners;

  /**
   * Map of <code>{@link CachingModel}</code>s that specify how to store,
   * retrieve and remove objects from the cache. Each cache model is stored
   * under a unique id (a String).
   */
  private Map cachingModels;

  protected final Log logger = LogFactory.getLog(getClass());

  public AbstractCachingInterceptor() {
    super();
  }

  /**
   * @throws FatalCacheException
   *           if the cache provider facade is <code>null</code>.
   * @throws FatalCacheException
   *           if the map of caching models is <code>null</code> or empty.
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

    CacheModelValidator validator = cacheProviderFacade
        .getCacheModelValidator();

    if (cachingModels instanceof Properties) {
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
            "Unable to create the caching model with id " + Strings.quote(id),
            exception);
      }

      setCachingModels(newCachingModels);

    } else {
      String id = null;

      try {
        for (Iterator i = cachingModels.keySet().iterator(); i.hasNext();) {
          id = (String) i.next();
          Object cachingModel = cachingModels.get(id);
          validator.validateCachingModel(cachingModel);
        }
      } catch (Exception exception) {
        throw new FatalCacheException(
            "Unable to validate caching model with id " + Strings.quote(id),
            exception);
      }
    }

    if (cacheKeyGenerator == null) {
      cacheKeyGenerator = new HashCodeCacheKeyGenerator(true);
      setCacheKeyGenerator(cacheKeyGenerator);
    }

    onAfterPropertiesSet();
  }

  protected final CacheKeyGenerator getCacheKeyGenerator() {
    return cacheKeyGenerator;
  }

  protected final Map getCachingModels() {
    return cachingModels;
  }

  protected abstract CachingModel getModel(MethodInvocation methodInvocation);

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
        if (ArrayUtils.hasElements(cachingListeners)) {
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
   * Gives subclasses the opportunity to set up their own properties.
   * 
   * @throws FatalCacheException
   *           if one or more properties of this interceptor contain invalid
   *           values or have an illegal state.
   */
  protected void onAfterPropertiesSet() throws FatalCacheException {
    // no implementation.
  }

  public final void setCacheKeyGenerator(CacheKeyGenerator newCacheKeyGenerator) {
    cacheKeyGenerator = newCacheKeyGenerator;
  }

  public final void setCacheProviderFacade(
      CacheProviderFacade newCacheProviderFacade) {
    cacheProviderFacade = newCacheProviderFacade;
  }

  public final void setCachingListeners(CachingListener[] newCachingListeners) {
    cachingListeners = newCachingListeners;
  }

  public final void setCachingModels(Map newCachingModels) {
    cachingModels = newCachingModels;
  }
}