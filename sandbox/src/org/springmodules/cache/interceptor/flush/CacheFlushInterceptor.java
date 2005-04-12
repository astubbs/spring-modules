/* 
 * Created on Oct 21, 2004
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

package org.springmodules.cache.interceptor.flush;

import java.lang.reflect.Method;
import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springmodules.cache.provider.CacheProviderFacade;

/**
 * <p>
 * Flushes part(s) of the cache when the intercepted method is executed.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:44 $
 */
public final class CacheFlushInterceptor extends CacheFlushAspectSupport
    implements MethodInterceptor {

  /**
   * Cache provider.
   */
  private CacheProviderFacade cacheProviderFacade;

  /**
   * Constructor.
   */
  public CacheFlushInterceptor() {
    super();
  }

  /**
   * Returns the metadata attribute of the intercepted method.
   * 
   * @param methodInvocation
   *          the description of an invocation to the method.
   * @return the metadata attribute of the intercepted method.
   */
  protected FlushCache getCacheFlushAttribute(MethodInvocation methodInvocation) {

    Method method = methodInvocation.getMethod();

    Object thisObject = methodInvocation.getThis();
    Class targetClass = (thisObject != null) ? thisObject.getClass() : null;

    CacheFlushAttributeSource attributeSource = super
        .getCacheFlushAttributeSource();
    FlushCache attribute = attributeSource.getCacheFlushAttribute(method,
        targetClass);
    return attribute;
  }

  /**
   * Flushes the part(s) of the cache.
   * 
   * @param methodInvocation
   *          the description of the intercepted method.
   * @return the return value of the intercepted method.
   */
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    FlushCache attribute = this.getCacheFlushAttribute(methodInvocation);

    if (null == attribute) {
      return methodInvocation.proceed();
    }

    String[] cacheProfileIds = attribute.getCacheProfileIds();
    Object proceedReturnValue = null;

    boolean flushedBeforeExecution = attribute.isFlushBeforeExecution();
    if (flushedBeforeExecution) {
      this.cacheProviderFacade.flushCache(cacheProfileIds);
      proceedReturnValue = methodInvocation.proceed();
    } else {
      proceedReturnValue = methodInvocation.proceed();
      this.cacheProviderFacade.flushCache(cacheProfileIds);
    }

    return proceedReturnValue;
  }

  /**
   * Set properties with method names as keys and caching-attribute descriptors
   * (parsed via <code>{@link CacheFlushAttributeEditor}</code>) as values.
   * <p>
   * Note: Method names are always applied to the target class, no matter if
   * defined in an interface or the class itself.
   * <p>
   * Internally, a <code>{@link NameMatchCacheFlushAttributeSource}</code>
   * will be created from the given properties.
   * 
   * @see NameMatchCacheFlushAttributeSource
   */
  public void setCacheFlushAttributes(Properties cacheFlushAttributes) {
    NameMatchCacheFlushAttributeSource attributeSource = new NameMatchCacheFlushAttributeSource();
    attributeSource.setProperties(cacheFlushAttributes);
    super.setCacheFlushAttributeSource(attributeSource);
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

}