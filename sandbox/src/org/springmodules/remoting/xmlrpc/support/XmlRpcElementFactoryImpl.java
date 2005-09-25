/* 
 * Created on Jul 5, 2005
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
package org.springmodules.remoting.xmlrpc.support;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * <p>
 * Default implementation of <code>{@link XmlRpcElementFactory}</code>
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XmlRpcElementFactoryImpl implements XmlRpcElementFactory {

  public XmlRpcElementFactoryImpl() {
    super();
  }

  /**
   * Creates a new <code>{@link XmlRpcArray}</code> from the specified
   * <code>java.util.Collection</code>.
   * 
   * @param source
   *          the collection to create the XML-RPC element from.
   * @return the created XML-RPC array.
   */
  private XmlRpcArray createXmlRpcArray(Collection source) {
    XmlRpcArray xmlRpcArray = new XmlRpcArray();

    Iterator iterator = source.iterator();
    while (iterator.hasNext()) {
      Object entry = iterator.next();
      XmlRpcElement xmlRpcCollectionElement = createXmlRpcElement(entry);
      xmlRpcArray.add(xmlRpcCollectionElement);
    }

    return xmlRpcArray;
  }

  /**
   * Creates a new <code>{@link XmlRpcArray}</code> from the specified object,
   * which should be an array.
   * 
   * @param source
   *          the object to create the XML-RPC element from.
   * @return the created XML-RPC array.
   */
  private XmlRpcArray createXmlRpcArray(Object source) {
    XmlRpcArray xmlRpcArray = new XmlRpcArray();

    int arrayLength = Array.getLength(source);
    for (int i = 0; i < arrayLength; i++) {
      Object element = Array.get(source, i);

      XmlRpcElement xmlRpcArrayElement = createXmlRpcElement(element);
      xmlRpcArray.add(xmlRpcArrayElement);
    }

    return xmlRpcArray;
  }

  /**
   * @see XmlRpcElementFactory#createXmlRpcElement(java.lang.Object)
   */
  public XmlRpcElement createXmlRpcElement(Object source) {
    XmlRpcElement xmlRpcElement = null;
    if (source == null) {
      xmlRpcElement = new XmlRpcString("");

    } else {
      Class sourceClass = source.getClass();

      if (sourceClass.isArray()) {
        Class componentType = sourceClass.getComponentType();

        if (componentType.equals(Byte.TYPE)) {
          xmlRpcElement = new XmlRpcBase64((byte[]) source);

        } else {
          xmlRpcElement = createXmlRpcArray(source);
        }

      } else {
        if (source instanceof Boolean) {
          xmlRpcElement = new XmlRpcBoolean((Boolean) source);

        } else if (source instanceof Character) {
          xmlRpcElement = new XmlRpcString((Character) source);

        } else if (source instanceof Collection) {
          xmlRpcElement = createXmlRpcArray((Collection) source);

        } else if (source instanceof Date) {
          xmlRpcElement = new XmlRpcDateTime((Date) source);

        } else if (source instanceof Double) {
          xmlRpcElement = new XmlRpcDouble((Double) source);

        } else if (source instanceof Float) {
          xmlRpcElement = new XmlRpcDouble((Float) source);

        } else if (source instanceof Integer) {
          xmlRpcElement = new XmlRpcInteger((Integer) source);

        } else if (source instanceof Long) {
          xmlRpcElement = new XmlRpcString((Long) source);

        } else if (source instanceof Map) {
          xmlRpcElement = createXmlRpcStruct((Map) source);

        } else if (source instanceof Short) {
          xmlRpcElement = new XmlRpcInteger((Short) source);

        } else if (source instanceof String) {
          xmlRpcElement = new XmlRpcString((String) source);

        } else {
          xmlRpcElement = createXmlRpcStruct(source);
        }
      }
    }

    return xmlRpcElement;
  }

  /**
   * Creates a new <code>{@link XmlRpcStruct}</code> from the specified
   * <code>java.util.Map</code>.
   * 
   * @param source
   *          the map to create the XML-RPC element from.
   * @return the created XML-RPC struct.
   */
  private XmlRpcStruct createXmlRpcStruct(Map source) {
    XmlRpcStruct xmlRpcStruct = new XmlRpcStruct();

    Iterator entrySetIterator = source.entrySet().iterator();
    while (entrySetIterator.hasNext()) {
      Map.Entry entry = (Map.Entry) entrySetIterator.next();
      Object entryValue = entry.getValue();
      XmlRpcElement xmlRpcValue = createXmlRpcElement(entryValue);

      xmlRpcStruct.add(entry.getKey().toString(), xmlRpcValue);
    }

    return xmlRpcStruct;
  }

  /**
   * Creates a new <code>{@link XmlRpcStruct}</code> from the specified
   * JavaBean.
   * 
   * @param source
   *          the JavaBean to create the XML-RPC element from.
   * @return the created XML-RPC struct.
   */
  private XmlRpcStruct createXmlRpcStruct(Object source) {
    XmlRpcStruct xmlRpcStruct = new XmlRpcStruct();

    BeanWrapper beanWrapper = new BeanWrapperImpl(source);
    PropertyDescriptor[] propertyDescriptors = beanWrapper
        .getPropertyDescriptors();

    int propertyDescriptorCount = propertyDescriptors.length;
    for (int i = 0; i < propertyDescriptorCount; i++) {
      PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
      String propertyName = propertyDescriptor.getName();

      if (!"class".equalsIgnoreCase(propertyName)) {
        Object propertyValue = beanWrapper.getPropertyValue(propertyName);

        XmlRpcElement xmlRpcValue = createXmlRpcElement(propertyValue);
        xmlRpcStruct.add(propertyName, xmlRpcValue);
      }
    }

    return xmlRpcStruct;
  }
}
