/* 
 * Created on Apr 7, 2006
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
package org.springmodules.cache.impl;

import java.io.Serializable;

/**
 * Template for implementations of <code>{@link Cache}</code>.
 * 
 * @author Alex Ruiz
 */
public abstract class AbstractCache implements Cache {

  /**
   * @see Cache#containsKey(Serializable)
   */
  public final boolean containsKey(Serializable key)
      throws IllegalArgumentException {
    validateKey(key);
    return doContainsKey(key);
  }

  /**
   * @see Cache#get(Serializable)
   */
  public final Serializable get(Serializable key)
      throws IllegalArgumentException {
    validateKey(key);
    Element element = doGet(key);

    Serializable value = null;
    if (element != null && element.isAlive()) {
      value = nullSafeGetValue(element);
    }
    return value;
  }

  /**
   * @see Cache#put(Serializable, Serializable)
   */
  public final Serializable put(Serializable key, Serializable value)
      throws IllegalArgumentException {
    validateKey(key);
    Element element = new Element(key, value);
    Element old = doPut(element);
    return nullSafeGetValue(old);
  }

  /**
   * @see Cache#put(Serializable, Serializable, long)
   */
  public final Serializable put(Serializable key, Serializable value,
      long timeToLive) throws IllegalArgumentException {
    validateKey(key);
    Element element = new Element(key, value, timeToLive);
    Element old = doPut(element);
    return nullSafeGetValue(old);
  }

  /**
   * @see Cache#remove(Serializable)
   */
  public final Serializable remove(Serializable key)
      throws IllegalArgumentException {
    validateKey(key);
    Element removed = doRemove(key);
    return nullSafeGetValue(removed);
  }

  /**
   * Returns <code>true</code> if this cache contains a mapping for the
   * specified key.
   * 
   * @param key
   *          key whose presence in this map is to be tested
   * @return <code>true</code> if this map contains a mapping for the
   *         specified key
   * 
   * @see Cache#containsKey(Serializable)
   */
  protected abstract boolean doContainsKey(Serializable key);

  /**
   * Returns the value to which this cache maps the specified key. Returns
   * <code>null</code> if the cache contains no mapping for this key. A return
   * value of <code>null</code> does not <i>necessarily</i> indicate that the
   * cache contains no mapping for the key; it's also possible that the cache
   * explicitly maps the key to <code>null</code>. The
   * <code>containsKey</code> operation may be used to distinguish these two
   * cases.
   * 
   * @param key
   *          key whose associated value is to be returned
   * @return the value to which this cache maps the specified key, or
   *         <code>null</code> if the map contains no mapping for this key
   * 
   * @see Cache#containsKey(Serializable)
   */
  protected abstract Element doGet(Serializable key);

  /**
   * Stores the given element in the cache.
   * 
   * @param element
   *          the element to store in the cache
   * @return previous element associated with specified key, or
   *         <code>null</code> if there was no mapping for key. A
   *         <code>null</code> return can also indicate that the map
   *         previously associated <code>null</code> with the specified key,
   *         if the implementation supports <code>null</code> values
   * 
   * @see Cache#put(Serializable, Serializable)
   * @see Cache#put(Serializable, Serializable, long)
   */
  protected abstract Element doPut(Element element);

  /**
   * Removes the cache element for this key from this cache if it is present.
   * 
   * @param key
   *          key whose element is to be removed from the cache
   * @return previous value associated with specified key, or <code>null</code>
   *         if there was no mapping for key
   * 
   * @throws IllegalArgumentException
   *           if the key is <code>null</code>
   * 
   * @see Cache#remove(Serializable)
   */
  protected abstract Element doRemove(Serializable key);

  /**
   * Returns the value of the given element if the element is not
   * <code>null</code>.
   * 
   * @param element
   *          the given element
   * @return the value of the given element or <code>null</code> if the given
   *         element is <code>null</code>
   */
  private Serializable nullSafeGetValue(Element element) {
    Serializable value = null;
    if (element != null) {
      value = element.getValue();
    }
    return value;
  }

  /**
   * Validates that the given key is not <code>null</code>.
   * 
   * @param key
   *          the key to validate
   * @throws IllegalArgumentException
   *           if the given key is <code>null</code>
   */
  private void validateKey(Serializable key) throws IllegalArgumentException {
    if (key == null) {
      throw new IllegalArgumentException("Element keys should not be null");
    }
  }
}
