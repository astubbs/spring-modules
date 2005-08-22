/* 
 * Created on Aug 17, 2005
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
package org.springmodules.cache.serializable;

import java.io.Serializable;

/**
 * <p>
 * Receives objects that do not implement <code>java.io.Serializable</code>
 * and makes them serializable. This object manipulation is useful when using
 * cache providers that can only store serializable objects (e.g. EHCache, JCS.)
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public interface SerializableFactory {

  /**
   * Makes the given object serializable.
   * 
   * @param obj
   *          the object to make serializable.
   * @return the result of the transformation of the given object into a
   *         serializable object.
   */
  Serializable makeSerializableIfNecessary(Object obj);

  /**
   * Returns the original object that was modified by
   * <code>{@link #makeSerializableIfNecessary(Object)}</code>.
   * 
   * @param obj
   *          the object that could be the result of a manipulation to make it
   *          serializable.
   * @return the original object that was forced to be serializable.
   */
  Object getOriginalValue(Object obj);
}
