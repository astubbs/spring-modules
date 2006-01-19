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

import com.thoughtworks.xstream.XStream;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import org.springmodules.util.Objects;

/**
 * <p>
 * Implementation of <code>{@link SerializableFactory}</code> that uses <a
 * href="http://xstream.codehaus.org">XStream</a> to make objects serializable.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class XStreamSerializableFactory implements SerializableFactory {

  /**
   * Wraps an XML-serialized object.
   */
  public static class ObjectWrapper implements Serializable {

    private static final long serialVersionUID = -1308206556015427863L;

    private Serializable value;

    /**
     * Constructor.
     */
    public ObjectWrapper() {
      super();
    }

    /**
     * Constructor.
     * 
     * @param value
     *          the new value to wrap
     */
    public ObjectWrapper(Serializable value) {
      this();
      setValue(value);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof ObjectWrapper)) {
        return false;
      }

      ObjectWrapper wrapper = (ObjectWrapper) obj;
      if (!ObjectUtils.nullSafeEquals(value, wrapper.value)) {
        return false;
      }

      return true;
    }

    /**
     * @return the wrapped value
     */
    public Serializable getValue() {
      return value;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
      int multiplier = 31;
      int hash = 17;

      hash = multiplier * hash + Objects.nullSafeHashCode(value);

      return hash;
    }

    /**
     * Sets the new value to wrap
     * 
     * @param newValue
     *          the new value
     */
    public void setValue(Serializable newValue) {
      value = newValue;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
      StringBuffer buffer = Objects.identityToString(this);
      buffer.append("[value=" + StringUtils.quoteIfString(value) + "]");

      return buffer.toString();
    }
  }

  private XStream xstream;

  /**
   * Constructor.
   */
  public XStreamSerializableFactory() {
    super();
    xstream = new XStream();
  }

  /**
   * @see SerializableFactory#getOriginalValue(Object)
   */
  public Object getOriginalValue(Object obj) {
    if (!(obj instanceof ObjectWrapper)) {
      return obj;
    }

    ObjectWrapper wrapper = (ObjectWrapper) obj;
    return wrapper.getValue();
  }

  /**
   * @see SerializableFactory#makeSerializableIfNecessary(Object)
   */
  public Serializable makeSerializableIfNecessary(Object obj) {
    if (obj == null || obj instanceof Serializable) {
      return (Serializable) obj;
    }

    String serializable = xstream.toXML(obj);
    return new ObjectWrapper(serializable);
  }

}
