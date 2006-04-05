/* 
 * Created on Apr 5, 2006
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import org.springmodules.util.Objects;

/**
 * <p>
 * A cache element.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class Element implements Serializable, Cloneable {

  private static final long EXPIRY_NEVER = -1l;

  private static Log logger = LogFactory.getLog(Element.class);

  private static final long serialVersionUID = -935757449385127201L;

  private long creationTime;

  private final Serializable key;

  private long timeToLive;

  private Serializable value;

  /**
   * Constructor.
   * 
   * @param newKey
   *          the new key for this entry
   * @param newValue
   *          the new value for this entry
   */
  public Element(Serializable newKey, Serializable newValue) {
    this(newKey, newValue, EXPIRY_NEVER);
  }

  /**
   * Constructor.
   * 
   * @param newKey
   *          the new key for this entry
   * @param newValue
   *          the new value for this entry
   * @param newTimeToLive
   *          the number of milliseconds until the cache entry will expire
   */
  public Element(final Serializable newKey, Serializable newValue,
      long newTimeToLive) {
    super();
    key = copy(newKey);
    setValue(newValue);
    creationTime = System.currentTimeMillis();
    timeToLive = newTimeToLive;
  }

  /**
   * @see java.lang.Object#clone()
   */
  public Object clone() {
    Element newElement = new Element(key, value);
    newElement.creationTime = creationTime;

    return newElement;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof Element)) {
      return false;
    }
    Element other = (Element) obj;
    if (!ObjectUtils.nullSafeEquals(key, other.key)) {
      return false;
    }
    if (!ObjectUtils.nullSafeEquals(value, other.value)) {
      return false;
    }
    return true;
  }

  public final long getCreationTime() {
    return creationTime;
  }

  public final Serializable getKey() {
    return key;
  }

  public final long getTimeToLive() {
    return timeToLive;
  }

  public final Serializable getValue() {
    return value;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    int prime = 31;
    int hash = 17;
    hash = prime * hash + Objects.nullSafeHashCode(key);
    hash = prime * hash + Objects.nullSafeHashCode(value);
    return hash;
  }

  public boolean isAlive() {
    if (timeToLive == EXPIRY_NEVER) {
      return true;
    }

    long currentTime = System.currentTimeMillis();
    long delta = currentTime - creationTime;

    return delta < timeToLive;
  }

  public final void setValue(Serializable newValue) {
    value = copy(newValue);
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer buffer = Objects.identityToString(this);
    buffer.append("[key=" + StringUtils.quoteIfString(key) + ", ");
    buffer.append("value=" + StringUtils.quoteIfString(value) + ", ");
    buffer.append("creationTime=" + new Date(creationTime) + ", ");
    buffer.append("timeToLive=" + timeToLive + "]");
    return buffer.toString();
  }

  private Serializable copy(Serializable oldValue) {
    Serializable newValue = null;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;

    try {
      oos = new ObjectOutputStream(baos);
      oos.writeObject(oldValue);

      ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());

      ois = new ObjectInputStream(bin);
      newValue = (Serializable) ois.readObject();

    } catch (Exception exception) {
      logger.error("Error cloning element " + toString(), exception);

    } finally {
      try {
        if (oos != null) {
          oos.close();
        }
        if (ois != null) {
          ois.close();
        }
      } catch (Exception exception) {
        logger.error("Error closing input/output streams", exception);
      }
    }
    return newValue;
  }

}
