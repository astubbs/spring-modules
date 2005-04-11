/* 
 * Created on Nov 29, 2004
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
 * Copyright @2004 the original author or authors.
 */

package org.springmodules.cache.provider.jcs;

import java.io.Serializable;

import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.behavior.ICacheElement;

/**
 * <p>
 * Subclass of <code>org.apache.jcs.engine.CacheElement</code> that overrides
 * the <code>equals(Object)</code> method to make the testing of
 * <code>{@link JcsFacade}</code> easier.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/11 04:02:13 $
 */
public class JcsCacheElement extends CacheElement {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3545793273739096113L;

  /**
   * Constructor.
   * 
   * @param cacheName
   *          the name of the cache.
   * @param key
   *          the key of the entry.
   * @param val
   *          the value to wrap.
   */
  public JcsCacheElement(String cacheName, Serializable key, Serializable val) {
    super(cacheName, key, val);
  }

  /**
   * Constructor.
   * 
   * @param cacheName
   *          the name of the cache.
   * @param key
   *          the key of the entry.
   * @param val
   *          the value to wrap.
   */
  public JcsCacheElement(String cacheName, Serializable key, Object val) {
    super(cacheName, key, val);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * @see #hashCode()
   */
  public boolean equals(Object obj) {

    boolean equals = true;

    if (null == obj || !(obj instanceof ICacheElement)) {
      equals = false;
    } else if (this != obj) {
      equals = this.toString().equals(obj.toString());
    }

    return equals;
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   */
  public int hashCode() {
    // override hashCode() to please PMD.
    return super.hashCode();
  }
}