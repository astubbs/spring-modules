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
 * @version $Revision: 1.1 $ $Date: 2005/06/17 09:57:47 $
 */
public class XmlRpcStruct implements XmlRpcElement {

  /**
   * Represents a member of a struct.
   */
  public static class XmlRpcMember {
    /**
     * The name of this member.
     */
    public final String name;

    /**
     * The value of this member.
     */
    public final XmlRpcElement value;

    /**
     * Constructor.
     * 
     * @param name
     *          the new name of this member.
     * @param value
     *          the new value of this member.
     */
    public XmlRpcMember(String name, XmlRpcElement value) {
      super();
      this.name = name;
      this.value = value;
    }
  }

  /**
   * The names of the members of this struct.
   */
  private List memberNames;

  /**
   * The members of this struct.
   */
  private List members;

  /**
   * Constructor.
   */
  public XmlRpcStruct() {
    super();
    this.memberNames = new ArrayList();
    this.members = new ArrayList();
  }

  /**
   * Adds a new member to this struct.
   * 
   * @param member
   *          the member to add.
   */
  public void add(XmlRpcMember member) {
    this.memberNames.add(member.name);
    this.members.add(member);
  }

  /**
   * Returns the names of the members of this struct.
   * 
   * @return the names of the members of this struct.
   */
  public String[] getMemberNames() {
    return (String[]) this.memberNames.toArray(new String[this.memberNames
        .size()]);
  }

  /**
   * Returns the members of this struct.
   * 
   * @return the members of this struct.
   */
  public XmlRpcMember[] getMembers() {
    return (XmlRpcMember[]) this.members.toArray(new XmlRpcMember[this.members
        .size()]);
  }

  /**
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class targetType) {
    Object matchingValue = NOT_MATCHING;

    if (Map.class.equals(targetType) || HashMap.class.equals(targetType)) {
      Map map = new HashMap();

      int memberCount = this.members.size();
      for (int i = 0; i < memberCount; i++) {
        XmlRpcMember member = (XmlRpcMember) this.members.get(i);
        map.put(member.name, member.value);
      }

      matchingValue = map;

    } else {
      BeanWrapper beanWrapper = new BeanWrapperImpl(targetType);
      boolean matching = true;

      int memberCount = this.members.size();
      for (int i = 0; i < memberCount; i++) {
        XmlRpcMember member = (XmlRpcMember) this.members.get(i);

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

      if (matching) {
        matchingValue = beanWrapper.getWrappedInstance();
      }
    }

    return matchingValue;
  }

}
