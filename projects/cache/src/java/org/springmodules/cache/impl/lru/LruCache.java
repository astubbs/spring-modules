/* 
 * Created on Apr 9, 2006
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
package org.springmodules.cache.impl.lru;

import java.io.Serializable;

import org.springmodules.cache.impl.Cache;
import org.springmodules.cache.impl.Element;


/**
 * TODO Describe this class
 * 
 * @author Alex Ruiz
 */
public class LruCache implements Cache {

  private static final int DEFAULT_MAX_CAPACITY = 20;

  private static final long serialVersionUID = -8650628618352862030L;

  private LruCacheEntry header;

  private final int maxCapacity;

  /**
   * Constructor.
   */
  public LruCache() {
    this(DEFAULT_MAX_CAPACITY);
  }

  /**
   * Constructor.
   * 
   * @param newMaxCapacity
   *          the maximum number of elements to store in this cache
   */
  public LruCache(int newMaxCapacity) {
    maxCapacity = newMaxCapacity;
    header = new LruCacheEntry();
    header.before = header.after = header;
  }

  /**
   * @see Cache#containsKey(Serializable)
   */
  public final boolean containsKey(Serializable key)
      throws IllegalArgumentException {
    validateKey(key);
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @see Cache#get(Serializable)
   */
  public final Serializable get(Serializable key)
      throws IllegalArgumentException {
    validateKey(key);
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see Cache#isEmpty()
   */
  public final boolean isEmpty() {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * @see Cache#put(Serializable, Serializable)
   */
  public final Serializable put(Serializable key, Serializable value)
      throws IllegalArgumentException {
    validateKey(key);
    Element element = new Element(key, value);
    return put(element);
  }

  /**
   * @see Cache#put(Serializable, Serializable, long)
   */
  public final Serializable put(Serializable key, Serializable value,
      long timeToLive) throws IllegalArgumentException {
    validateKey(key);
    Element element = new Element(key, value, timeToLive);
    return put(element);
  }

  /**
   * @see Cache#remove(Serializable)
   */
  public final Serializable remove(Serializable key)
      throws IllegalArgumentException {
    validateKey(key);
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see Cache#size()
   */
  public final int size() {
    // TODO Auto-generated method stub
    return 0;
  }

  protected final LruCacheEntry getHeader() {
    return header;
  }

  private int hash(Object obj) {
    int hash = obj.hashCode();
    hash += ~(hash << 9);
    hash ^= (hash >>> 14);
    hash += (hash << 4);
    hash ^= (hash >>> 10);
    return hash;
  }

  private Serializable put(Element element) {
    return null;
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
