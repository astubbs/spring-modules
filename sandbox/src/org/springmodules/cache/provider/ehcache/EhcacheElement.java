/* 
 * Created on Dec 4, 2004
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

package org.springmodules.cache.provider.ehcache;

import java.io.Serializable;

import net.sf.ehcache.Element;

/**
 * <p>
 * Subclass of <code>net.sf.ehcache.Element</code> that overrides the
 * <code>equals(Object)</code> method to make the testing of
 * <code>{@link EhcacheFacade}</code> easier.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/12 08:37:47 $
 */
public class EhcacheElement extends Element {

  /**
   * Version number of this class.
   * 
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 3256723974527727160L;

  /**
   * Constructor.
   * 
   * @param key
   *          the key of the cache entry.
   * @param value
   *          the value to store in the cache.
   */
  public EhcacheElement(Serializable key, Serializable value) {
    super(key, value);
  }

  /**
   * Constructor.
   * 
   * @param key
   *          the key of the cache entry.
   * @param value
   *          the value to store in the cache.
   * @param version
   *          the version of the cache entry.
   */
  public EhcacheElement(Serializable key, Serializable value, long version) {
    super(key, value, version);
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

    if (null == obj || !(obj instanceof Element)) {
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