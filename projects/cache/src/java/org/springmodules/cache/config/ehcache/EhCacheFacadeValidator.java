/* 
 * Created on Feb 20, 2006
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
package org.springmodules.cache.config.ehcache;

import org.springframework.beans.factory.support.AbstractBeanDefinition;

import org.springmodules.cache.config.CacheProviderFacadeValidator;
import org.springmodules.cache.provider.ehcache.EhCacheFacade;

/**
 * <p>
 * Validates that a bean definition describes a
 * <code>{@link EhCacheFacade}</code>..
 * </p>
 * 
 * @author Alex Ruiz
 */
public final class EhCacheFacadeValidator implements
    CacheProviderFacadeValidator {

  /**
   * @see CacheProviderFacadeValidator#validate(AbstractBeanDefinition)
   */
  public void validate(AbstractBeanDefinition cacheProviderFacade)
      throws IllegalStateException {
    Class expectedClass = EhCacheFacade.class;
    Class actualClass = cacheProviderFacade.getBeanClass();

    if (!(expectedClass.isAssignableFrom(actualClass))) {
      throw new IllegalStateException(
          "The cache provider facade should be an instance of "
              + expectedClass.getName());
    }
  }

}
