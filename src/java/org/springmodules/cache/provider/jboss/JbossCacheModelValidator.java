/* 
 * Created on Sep 6, 2005
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
package org.springmodules.cache.provider.jboss;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.CacheModelValidator;
import org.springmodules.cache.provider.InvalidCacheModelException;
import org.springmodules.util.ArrayUtils;

/**
 * <p>
 * Validates the properties of a <code>{@link JbossCacheCachingModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public final class JbossCacheModelValidator implements CacheModelValidator {

  public JbossCacheModelValidator() {
    super();
  }

  /**
   * @throws InvalidCacheModelException
   *           if the model is not an instance of
   *           <code>JbossCacheCachingModel</code>.
   * @throws InvalidCacheModelException
   *           if the model does not have a node FQN.
   * @see CacheModelValidator#validateCachingModel(Object)
   */
  public void validateCachingModel(Object model)
      throws InvalidCacheModelException {
    if (!(model instanceof JbossCacheCachingModel)) {
      throw new InvalidCacheModelException(
          "The caching model should be an instance of <"
              + JbossCacheCachingModel.class.getName() + ">");
    }
    JbossCacheCachingModel cachingModel = (JbossCacheCachingModel) model;
    if (!StringUtils.hasText(cachingModel.getNode())) {
      throw new InvalidCacheModelException(
          "The FQN of the cache node should not be empty");
    }
  }

  /**
   * @throws InvalidCacheModelException
   *           if the model is not an instance of
   *           <code>JbossCacheFlushingModel</code>.
   * @throws InvalidCacheModelException
   *           if the model does not have at least one nodeFQN.
   * @see CacheModelValidator#validateFlushingModel(Object)
   */
  public void validateFlushingModel(Object model)
      throws InvalidCacheModelException {
    if (!(model instanceof JbossCacheFlushingModel)) {
      throw new InvalidCacheModelException(
          "The flushing model should be an instance of <"
              + JbossCacheFlushingModel.class.getName() + ">");
    }

    JbossCacheFlushingModel flushingModel = (JbossCacheFlushingModel) model;
    String[] nodeFqns = flushingModel.getNodes();
    if (!ArrayUtils.hasElements(nodeFqns)) {
      throw new InvalidCacheModelException(
          "There should be at least one node FQN");
    }
  }

}
