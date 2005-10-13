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

import java.util.Iterator;
import java.util.Map;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FatalCacheException;
import org.springmodules.util.Strings;

/**
 * <p>
 * Caches the return value of the intercepted method. Intercepts the methods
 * that have <code>{@link CachingModel}</code>s associated to them.
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class MethodMapCachingInterceptor extends
    AbstractModelSourceCachingInterceptor {

  public MethodMapCachingInterceptor() {
    super();
  }

  /**
   * @see AbstractCachingInterceptor#onAfterPropertiesSet()
   */
  protected void onAfterPropertiesSet() throws FatalCacheException {
    CachingModelSource cachingModelSource = getCachingModelSource();

    if (cachingModelSource == null) {
      MethodMapCachingModelSource newSource = new MethodMapCachingModelSource();

      Map models = getCachingModels();
      FatalCacheException fatalCacheException = null;

      String key = null;
      try {
        for (Iterator i = models.keySet().iterator(); i.hasNext();) {
          key = (String) i.next();
          CachingModel model = (CachingModel) models.get(key);
          newSource.addCachingModel(key, model);
        }

      } catch (FatalCacheException exception) {
        fatalCacheException = exception;

      } catch (Exception exception) {
        fatalCacheException = new FatalCacheException(
            "Unable to add model stored under the key " + Strings.quote(key),
            exception);
      }

      if (fatalCacheException != null) {
        throw fatalCacheException;
      }

      setCachingModelSource(newSource);
    }
  }
}
