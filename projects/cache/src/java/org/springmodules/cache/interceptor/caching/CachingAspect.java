/* 
 * Created on Apr 23, 2006
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
 * Copyright @2004-2006 the original author or authors.
 */
package org.springmodules.cache.interceptor.caching;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.ConfigurationError;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.cache.key.CacheKeyGenerator;
import org.springmodules.cache.key.HashCodeCacheKeyGenerator;
import org.springmodules.cache.provider.CacheProviderFacade;

import org.springframework.util.ObjectUtils;

/**
 * Understands caching of the return value of an advised method.
 * 
 * @author Alex Ruiz
 */
public final class CachingAspect {

  public static final NullObject NULL_ENTRY = new NullObject();

  private static Log logger = LogFactory.getLog(CachingAspect.class);

  private final CacheProviderFacade cache;

  private final CacheKeyGenerator keyGenerator;

  private final CachingObserver[] observers;

  public CachingAspect(CacheProviderFacade c) {
    this(c, (CachingObserver[]) null);
  }
  
  public CachingAspect(CacheProviderFacade c, CachingObserver[] o) {
    this(c, null, o);
  }

  public CachingAspect(CacheProviderFacade c, CacheKeyGenerator k) {
    this(c, k, null);
  }
  
  public CachingAspect(CacheProviderFacade c, CacheKeyGenerator k,
      CachingObserver[] o) {
    cache = validate(c);
    keyGenerator = k != null ? k : defaultKeyGenerator();
    observers = o;
  }

  private CacheProviderFacade validate(CacheProviderFacade c) {
    if (c == null) ConfigurationError.missingRequiredProperty("cache");
    return c;
  }

  private CacheKeyGenerator defaultKeyGenerator() {
    return new HashCodeCacheKeyGenerator(true);
  }

  protected Object doCaching(MethodInvocation mi, CachingModel model)
      throws Throwable {
    Method advised = mi.getMethod();
    if (!CachingUtils.isCacheable(advised)) return methodNotCacheable(mi);
    if (model == null) return modelNotFound(mi);
    Serializable key = keyGenerator.generateKey(mi);
    return cachedValue(mi, key, model);
  }

  private Object cachedValue(MethodInvocation mi, Serializable key,
      CachingModel m) throws Throwable {
    Object cached = cache.get(key, m);
    if (cached != null) return unmaskNull(cached);
    boolean successful = true;
    try {
      cached = mi.proceed();
      cache.put(key, m, maskNull(cached));
      notifyListeners(key, cached, m);
    } catch (Throwable t) {
      successful = false;
      logger.error("Unable to proceed to the next interceptor in the chain", t);
      throw t;
    } finally {
      if (!successful) cache.cancelUpdate(key);
    }
    return cached;
  }

  private Object methodNotCacheable(MethodInvocation mi) throws Throwable {
    String reason = "Intercepted method <" + methodName(mi)
        + "> does not return a value";
    return cannotCache(reason, mi);
  }

  private Object modelNotFound(MethodInvocation mi) throws Throwable {
    String reason = "Unable to find model for the method <" + methodName(mi)
        + ">";
    return cannotCache(reason, mi);
  }

  private String methodName(MethodInvocation mi) {
    return mi.getMethod().getName();
  }

  private Object cannotCache(String reason, MethodInvocation mi)
      throws Throwable {
    logger.debug("Unable to perform caching. " + reason);
    return mi.proceed();
  }

  private void notifyListeners(Serializable key, Object cached, CachingModel m) {
    if (ObjectUtils.isEmpty(observers)) return;
    for (int i = 0; i < observers.length; i++)
      observers[i].onCaching(key, cached, m);
  }

  private Object maskNull(Object o) {
    return o != null ? o : NULL_ENTRY;
  }

  private Object unmaskNull(Object obj) {
    return NULL_ENTRY.equals(obj) ? null : obj;
  }

  Class keyGeneratorType() {
    return keyGenerator.getClass();
  }
}
