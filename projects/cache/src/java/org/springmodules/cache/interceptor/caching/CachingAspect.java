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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.interceptor.caching;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.key.CacheKeyGenerator;
import org.springmodules.cache.provider.CacheProviderFacade;

import org.springframework.util.ObjectUtils;

/**
 * TODO Describe this class
 * 
 * @author Alex Ruiz
 */
final class CachingAspect {

  public static final NullObject NULL_ENTRY = new NullObject();

  private static Log logger = LogFactory.getLog(CachingAspect.class);

  private CacheProviderFacade cache;

  private CacheKeyGenerator keyGenerator;

  private CachingListener[] listeners;

  Object doCaching(MethodInvocation mi, CachingModel m) throws Throwable {
    Method adviced = mi.getMethod();
    if (!CachingUtils.isCacheable(adviced)) return methodNotCacheable(mi);
    if (m == null) return modelNotFound(mi);
    Serializable key = keyGenerator.generateKey(mi);
    return cachedValue(mi, key, m);
  }

  private Object cachedValue(MethodInvocation mi, Serializable key,
      CachingModel m) throws Throwable {
    Object cached = cache.getFromCache(key, m);
    if (cached != null) return unmaskNull(cached);
    boolean successful = true;
    try {
      cached = maskNull(mi.proceed());
    } catch (Throwable t) {
      successful = false;
      logger.error("Unable to proceed to the next interceptor in the chain", t);
      throw t;
    } finally {
      if (!successful) cache.cancelCacheUpdate(key);
      cache.putInCache(key, m, cached);
      notifyListeners(key, cached, m);
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

  private Object cannotCache(String reason, MethodInvocation mi)
      throws Throwable {
    logger.debug("Unable to perform caching. " + reason);
    return mi.proceed();
  }

  private String methodName(MethodInvocation mi) {
    return mi.getMethod().getName();
  }

  private void notifyListeners(Serializable key, Object cached, CachingModel m) {
    if (ObjectUtils.isEmpty(listeners)) return;
    for (int i = 0; i < listeners.length; i++)
      listeners[i].onCaching(key, cached, m);
  }

  private Object maskNull(Object o) {
    return o != null ? o : NULL_ENTRY;
  }

  private Object unmaskNull(Object obj) {
    return NULL_ENTRY.equals(obj) ? null : obj;
  }

  void setCacheProviderFacade(CacheProviderFacade c) {
    cache = c;
  }

  void setCacheKeyGenerator(CacheKeyGenerator k) {
    keyGenerator = k;
  }

  void setCachingListeners(CachingListener[] l) {
    listeners = l;
  }
}
