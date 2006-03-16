/* 
 * Created on Mar 15, 2006
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
package org.springmodules.cache.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;

/**
 * <p>
 * Template for implementations of
 * <code>{@link CacheProviderFacadeValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCacheProviderFacadeDefinitionValidator implements
    CacheProviderFacadeValidator {

  /**
   * @see CacheProviderFacadeValidator#validate(AbstractBeanDefinition)
   */
  public final void validate(AbstractBeanDefinition cacheProviderFacade)
      throws IllegalStateException {
    Class expectedClass = getExpectedClass();
    Class actualClass = cacheProviderFacade.getBeanClass();

    if (!(expectedClass.isAssignableFrom(actualClass))) {
      throw new IllegalStateException(
          "The cache provider facade should be an instance of "
              + expectedClass.getName());
    }
  }

  protected void doValidate(AbstractBeanDefinition cacheProviderFacade) {
    // no implementation.
  }

  protected abstract Class getExpectedClass();
}
