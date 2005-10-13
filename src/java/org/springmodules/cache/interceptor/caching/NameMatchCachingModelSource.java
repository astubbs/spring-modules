/* 
 * Created on Oct 3, 2005
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
package org.springmodules.cache.interceptor.caching;

import java.lang.reflect.Method;
import java.util.Map;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.interceptor.AbstractNameMatchCacheModelSource;

/**
 * <p>
 * Simple implementation of <code>{@link CachingModelSource}</code> that
 * allows <code>{@link CachingModel}</code> to be matched by registered name.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class NameMatchCachingModelSource extends
    AbstractNameMatchCacheModelSource implements CachingModelSource {

  public NameMatchCachingModelSource() {
    super();
  }

  /**
   * @see CachingModelSource#getCachingModel(Method, Class)
   */
  public CachingModel getCachingModel(Method method, Class targetClass) {
    return (CachingModel) getCacheModel(method);
  }

  protected final Map getCachingModels() {
    return getCacheModels();
  }
  
  public void setCachingModels(Map models) {
    setCacheModels(models);
  }
}
