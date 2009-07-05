/* 
 * Created on Nov 4, 2004
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

package org.springmodules.cache.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springmodules.cache.CachingModel;
import org.springmodules.cache.interceptor.caching.CachingListener;

/**
 * <p>
 * Listener that stores in a collection the keys and caching models used to add
 * entries to the cache.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class KeyAndModelListCachingListener implements CachingListener {

  public static class KeyAndModel {

    public final Serializable key;

    public final CachingModel model;

    public KeyAndModel(Serializable newKey, CachingModel newModel) {
      super();
      key = newKey;
      model = newModel;
    }
  }

  private List keyAndModelPairs;

  public KeyAndModelListCachingListener() {
    super();
    this.keyAndModelPairs = new ArrayList();
  }

  public final List getKeyAndModelPairs() {
    return this.keyAndModelPairs;
  }

  /**
   * @see CachingListener#onCaching(Serializable, Object, CachingModel)
   */
  public final void onCaching(Serializable key, Object obj, CachingModel model) {
    this.keyAndModelPairs.add(new KeyAndModel(key, model));
  }
}