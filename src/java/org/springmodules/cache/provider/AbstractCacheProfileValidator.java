/* 
 * Created on Aug 5, 2005
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
package org.springmodules.cache.provider;

/**
 * <p>
 * Template for implementations of <code>{@link CacheProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class AbstractCacheProfileValidator implements
    CacheProfileValidator {

  public AbstractCacheProfileValidator() {
    super();
  }

  /**
   * Returns the type of cache profile to validate.
   * 
   * @return the type of the cache profile to validate.
   */
  protected abstract Class getTargetClass();

  /**
   * @throws InvalidCacheProfileException
   *           if the cache profile is <code>null</code>.
   * @throws InvalidCacheProfileException
   *           if the cache profile is not an instance of the target class.
   * 
   * @see CacheProfileValidator#validateCacheProfile(Object)
   * @see #getTargetClass()
   */
  public final void validateCacheProfile(Object cacheProfile)
      throws InvalidCacheProfileException {

    if (cacheProfile == null) {
      throw new InvalidCacheProfileException(
          "The cache profile should not be null");
    }

    Class targetClass = getTargetClass();
    if (!targetClass.equals(cacheProfile.getClass())) {
      throw new InvalidCacheProfileException(
          "The cache profile should be an instance of <"
              + targetClass.getName() + ">");
    }

    validateCacheProfileProperties(cacheProfile);
  }

  protected abstract void validateCacheProfileProperties(Object cacheProfile)
      throws InvalidCacheProfileException;

}
