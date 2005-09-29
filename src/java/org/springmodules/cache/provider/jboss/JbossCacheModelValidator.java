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
import org.springmodules.cache.provider.AbstractCacheModelValidator;
import org.springmodules.cache.provider.InvalidCacheModelException;

/**
 * <p>
 * Validates the properties of a <code>{@link JbossCacheModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class JbossCacheModelValidator extends AbstractCacheModelValidator {

  public JbossCacheModelValidator() {
    super();
  }

  /**
   * @see AbstractCacheModelValidator#getTargetClass()
   */
  protected Class getTargetClass() {
    return JbossCacheModel.class;
  }

  /**
   * @see AbstractCacheModelValidator#validateCacheModelProperties(java.lang.Object)
   */
  protected void validateCacheModelProperties(Object cacheModel)
      throws InvalidCacheModelException {
    JbossCacheModel jbossCacheModel = (JbossCacheModel) cacheModel;

    if (!StringUtils.hasText(jbossCacheModel.getNodeFqn())) {
      throw new InvalidCacheModelException(
          "The FQN of the cache node should not be empty");
    }
  }

}
