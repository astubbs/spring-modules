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

/**
 * <p>
 * Implementation of <code>{@link SerializableFactory}</code> that uses <a
 * href="http://xstream.codehaus.org">XStream</a> to make objects serializable.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XStreamSerializableFactory implements SerializableFactory {

  public static class ObjectWrapper implements Serializable {

    private static final long serialVersionUID = -1308206556015427863L;

    /**
     * Value to wrap.
     */
    private Serializable value;

    public ObjectWrapper() {
      super();
    }

    public ObjectWrapper(Serializable value) {
      this();
      setValue(value);
    }

    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof ObjectWrapper)) {
        return false;
      }

      ObjectWrapper wrapper = (ObjectWrapper) obj;
      if (this.value != null ? !this.value.equals(wrapper.value)
          : wrapper.value != null) {
        return false;
      }

      return true;
    }

    public Serializable getValue() {
      return this.value;
    }

    public int hashCode() {
      int multiplier = 31;
      int hash = 17;

      hash = multiplier * hash
          + (this.value != null ? this.value.hashCode() : 0);

      return hash;
    }

    public void setValue(Serializable value) {
      this.value = value;
    }

    public String toString() {
      StringBuffer buffer = new StringBuffer(getClass().getName());
      buffer.append("@" + System.identityHashCode(this) + "[");
      buffer.append("value=");

      if (this.value instanceof String) {
        buffer.append("'" + this.value + "'");
      } else {
        buffer.append(this.value);
      }

      buffer.append("]");
      return buffer.toString();
    }
  }

  private XStream xstream;

  public XStreamSerializableFactory() {
    super();
    this.xstream = new XStream();
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

    String serializable = this.xstream.toXML(obj);
    return new ObjectWrapper(serializable);
  }

}
