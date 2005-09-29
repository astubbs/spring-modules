/* 
 * Created on Jan 14, 2005
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

package org.springmodules.cache.provider.ehcache;

import org.springframework.util.StringUtils;
import org.springmodules.cache.provider.AbstractCacheModelValidator;
import org.springmodules.cache.provider.InvalidCacheModelException;

/**
 * <p>
 * Validates the properties of a <code>{@link EhCacheModel}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class EhCacheModelValidator extends AbstractCacheModelValidator {

  public EhCacheModelValidator() {
    super();
  }

  /**
   * @see AbstractCacheModelValidator#getTargetClass()
   */
  protected Class getTargetClass() {
    return EhCacheModel.class;
  }

  /**
   * @see AbstractCacheModelValidator#validateCacheModelProperties(java.lang.Object)
   */
  protected void validateCacheModelProperties(Object cacheModel)
      throws InvalidCacheModelException {
    EhCacheModel ehCacheModel = (EhCacheModel) cacheModel;

    if (!StringUtils.hasText(ehCacheModel.getCacheName())) {
      throw new InvalidCacheModelException("Cache name should not be empty");
    }
  }

}