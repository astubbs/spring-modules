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
 * Template for implementations of <code>{@link CacheModelValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public abstract class AbstractCacheModelValidator implements
    CacheModelValidator {

  public AbstractCacheModelValidator() {
    super();
  }

  /**
   * Returns the type of cache model to validate.
   * 
   * @return the type of the cache model to validate.
   */
  protected abstract Class getTargetClass();

  /**
   * @throws InvalidCacheModelException
   *           if the cache model is <code>null</code>.
   * @throws InvalidCacheModelException
   *           if the cache model is not an instance of the target class.
   * 
   * @see CacheModelValidator#validateCacheModel(Object)
   * @see #getTargetClass()
   */
  public final void validateCacheModel(Object cacheModel)
      throws InvalidCacheModelException {
    if (cacheModel == null) {
      throw new InvalidCacheModelException("The cache model should not be null");
    }

    Class targetClass = getTargetClass();
    if (!targetClass.equals(cacheModel.getClass())) {
      throw new InvalidCacheModelException(
          "The cache model should be an instance of <" + targetClass.getName()
              + ">");
    }

    validateCacheModelProperties(cacheModel);
  }

  protected abstract void validateCacheModelProperties(Object cacheModule)
      throws InvalidCacheModelException;

}
