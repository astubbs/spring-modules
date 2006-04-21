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
 * allows caching models to be matched by registered name.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class NameMatchCachingModelSource extends
    AbstractNameMatchCacheModelSource implements CachingModelSource {

  /**
   * @see CachingModelSource#getModel(Method, Class)
   */
  public CachingModel getModel(Method method, Class targetClass) {
    return (CachingModel) getCacheModel(method);
  }

  /**
   * Sets the map of caching models to use. Each map entry uses the name of the
   * method to advise as key (a String) and the caching model to bind as value.
   * 
   * @param models
   *          the new map of caching models
   */
  public void setCachingModels(Map models) {
    setCacheModels(models);
  }
}
