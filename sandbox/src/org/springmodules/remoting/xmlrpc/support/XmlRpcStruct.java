/* 
 * Created on Jun 14, 2005
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * <p>
 * Represents a complex data structure.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.9 $ $Date: 2005/09/25 05:19:58 $
 */
public final class XmlRpcStruct implements XmlRpcElement {

  /**
   * Represents a member of a struct.
   */
  public static final class XmlRpcMember {
    /**
     * The name of this member.
     */
    public final String name;

    /**
     * The value of this member.
     */
    public final XmlRpcElement value;

    public XmlRpcMember(String newName, XmlRpcElement newValue) {
      super();
      name = newName;
      value = newValue;
    }

    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof XmlRpcMember)) {
        return false;
      }

      final XmlRpcMember xmlRpcMember = (XmlRpcMember) obj;

      if (name != null ? !name.equals(xmlRpcMember.name)
          : xmlRpcMember.name != null) {
        return false;
      }
      if (value != null ? !value.equals(xmlRpcMember.value)
          : xmlRpcMember.value != null) {
        return false;
      }

      return true;
    }

    public int hashCode() {
      int hash = 7;
      hash = 31 * hash + (name != null ? name.hashCode() : 0);
      hash = 31 * hash + (value != null ? value.hashCode() : 0);
      return hash;
    }

    public String toString() {
      return "XmlRpcMember: name='" + name + "', value=" + value;
    }
  }

  /**
   * The members of this struct.
   */
  private List members;

  public XmlRpcStruct() {
    super();
    members = new ArrayList();
  }

  /**
   * Adds a new member to this struct.
   * 
   * @param name
   *          the name of the member.
   * @param value
   *          the value of the member.
   */
  public void add(String name, XmlRpcElement value) {
    XmlRpcMember member = new XmlRpcMember(name, value);
    add(member);
  }

  /**
   * Adds a new member to this struct.
   * 
   * @param member
   *          the member to add.
   */
  public void add(XmlRpcMember member) {
    members.add(member);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof XmlRpcStruct)) {
      return false;
    }

    final XmlRpcStruct xmlRpcStruct = (XmlRpcStruct) obj;

    if (members != null ? !members.equals(xmlRpcStruct.members)
        : xmlRpcStruct.members != null) {
      return false;
    }

    return true;
  }

  /**
   * Returns an instance of the specified target type. The target type should be
   * represent JavaBean. The names and types properties of the JavaBean should
   * match the ones from the members of this struct. Otherwise this method will
   * return <code>{@link XmlRpcElement#NOT_MATCHING}</code>.
   * 
   * @param targetType
   *          the target type (representing a JavaBean).
   * @return an instance of the specified target type.
   */
  private Object getBeanMatchingValue(Class targetType) {
    BeanWrapper beanWrapper = new BeanWrapperImpl(targetType);
    boolean matching = true;

    int memberCount = members.size();
    for (int i = 0; i < memberCount; i++) {
      XmlRpcMember member = (XmlRpcMember) members.get(i);

      String propertyName = member.name;
      if (beanWrapper.isWritableProperty(propertyName)) {
        Class propertyType = beanWrapper.getPropertyType(propertyName);
        Object propertyValue = member.value.getMatchingValue(propertyType);

        if (propertyValue == NOT_MATCHING) {
          matching = false;
          break;
        }

        beanWrapper.setPropertyValue(propertyName, propertyValue);

      } else {
        matching = false;
        break;
      }
    }

    Object matchingValue = null;
    if (matching) {
      matchingValue = beanWrapper.getWrappedInstance();
    } else {
      matchingValue = NOT_MATCHING;
    }

    return matchingValue;
  }

  /**
   * <p>
   * Returns a map containing the members of this struct. The names of the
   * members are used as entry keys and the values as entry values.
   * </p>
   * 
   * <p>
   * The value of the members should be instances of
   * <code>{@link XmlRpcScalar}</code>, otherwise this method will return
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code>.
   * </p>
   * 
   * @return a map containing the members of this struct.
   */
  private Object getMapMatchingValue() {
    boolean matching = true;
    Map map = new HashMap();

    int memberCount = members.size();
    for (int i = 0; i < memberCount; i++) {
      XmlRpcMember member = (XmlRpcMember) members.get(i);
      XmlRpcElement value = member.value;

      if (value instanceof XmlRpcScalar) {
        XmlRpcScalar scalarValue = (XmlRpcScalar) value;
        map.put(member.name, scalarValue.getValue());
      } else {
        matching = false;
        break;
      }
    }

    Object matchingValue = null;
    if (matching) {
      matchingValue = map;
    } else {
      matchingValue = NOT_MATCHING;
    }

    return matchingValue;
  }

  /**
   * Returns:
   * <ul>
   * <li>a map containing the members of this struct as entries. Only if the
   * specified type is <code>{@link Map}</code> or
   * <code>{@link HashMap}</code>.</li>
   * <li>a JavaBean. If the names and types of the JavaBean properties match
   * the members of this struct.</li>
   * <li>If none of the above conditions are met,
   * <code>{@link XmlRpcElement#NOT_MATCHING}</code>.</li>
   * </ul>
   * 
   * @see #getBeanMatchingValue(Class)
   * @see #getMapMatchingValue()
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (Map.class.equals(targetType) || HashMap.class.equals(targetType)) {
      matchingValue = getMapMatchingValue();

    } else {
      matchingValue = getBeanMatchingValue(targetType);
    }

    return matchingValue;
  }

  /**
   * Returns the members of this struct.
   * 
   * @return the members of this struct.
   */
  public XmlRpcMember[] getMembers() {
    return (XmlRpcMember[]) members.toArray(new XmlRpcMember[members.size()]);
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (members != null ? members.hashCode() : 0);
    return hash;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("members=" + members + "]");

    return buffer.toString();
  }
}
