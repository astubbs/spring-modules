/* 
 * Created on May 31, 2005
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
package org.springmodules.xmlrpc.serializer;

import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * <p>
 * Serializes a JavaBean into a <code>{@link Hashtable}</code>. The name of
 * the JavaBean property is used as the entry key and the serialized value of
 * the JavaBean property is used as the entry value.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 10:28:12 $
 */
public final class BeanSerializer extends AbstractXmlRpcSerializer {

  /**
   * The names of the properties to include in the serialized object.
   */
  private List properties;

  /**
   * The class this serializer can handle.
   */
  private Class supportedClass;

  /**
   * Constructor.
   */
  public BeanSerializer() {
    super();
  }

  /**
   * @see XmlRpcSerializer#getSupportedClass()
   */
  public Class getSupportedClass() {
    return this.supportedClass;
  }

  /**
   * @see XmlRpcSerializer#serialize(Object, XmlRpcSerializerRegistry)
   */
  public Object serialize(Object obj,
      XmlRpcSerializerRegistry serializerRegistry) {
    int propertyCount = this.properties.size();
    Hashtable hashtable = new Hashtable(propertyCount);

    if (obj != null) {
      BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
      for (int i = 0; i < propertyCount; i++) {
        String property = (String) this.properties.get(i);
        Object propertyValue = beanWrapper.getPropertyValue(property);

        XmlRpcSerializer serializer = serializerRegistry
            .findSerializer(propertyValue);

        Object serialized = serializer.serialize(propertyValue,
            serializerRegistry);
        hashtable.put(property, serialized);
      }
    }

    return hashtable;
  }

  /**
   * Setter for the field <code>{@link #properties}</code>.
   * 
   * @param properties the new value to set.
   */
  public final void setProperties(List properties) {
    this.properties = properties;
  }

  /**
   * Setter for the field <code>{@link #supportedClass}</code>.
   * 
   * @param supportedClass
   *          the new value to set.
   */
  public final void setSupportedClass(Class supportedClass) {
    this.supportedClass = supportedClass;
  }

}
