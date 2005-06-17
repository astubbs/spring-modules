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

/**
 * <p>
 * Represents a string.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/17 09:57:48 $
 */
public class XmlRpcString implements XmlRpcScalar {

  /**
   * The value of this scalar.
   */
  private String value;

  /**
   * Constructor.
   */
  public XmlRpcString() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcString(String value) {
    this();
    this.value = value;
  }

  /**
   * @see XmlRpcScalar#getValue()
   */
  public Object getValue() {
    return this.value;
  }

  /**
   * @see XmlRpcElement#getMatchingValue(Class)
   */
  public Object getMatchingValue(Class type) {
    Object matchingValue = NOT_MATCHING;

    if (String.class.equals(type)) {
      matchingValue = this.value;
    }
    else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
      try {
        matchingValue = new Long(this.value);
        
      } catch (NumberFormatException exception) {
        matchingValue = NOT_MATCHING;
      }
    }
    return matchingValue;
  }
}
