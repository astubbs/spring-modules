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
package org.springmodules.cache.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;

/**
 * <p>
 * Validator for bean definitions that describe instances of
 * <code>{@link org.springmodules.cache.provider.CacheProviderFacade}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public interface CacheProviderFacadeDefinitionValidator {

  /**
   * Validates that the given bean definition describes a valid
   * <code>{@link org.springmodules.cache.provider.CacheProviderFacade}</code>.
   * 
   * @param cacheProviderFacade
   *          the bean definition describing a cache provider facade
   * @throws IllegalStateException
   *           if the cache provider facade is not valid
   */
  void validate(AbstractBeanDefinition cacheProviderFacade)
      throws IllegalStateException;
}
