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
 * Represents a XML-RPC boolean value.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/20 05:02:12 $
 */
public class XmlRpcBoolean implements XmlRpcScalar {

  /**
   * Represents <code>false</code> in XML-RPC.
   */
  public static final String FALSE = "0";

  /**
   * Represents <code>true</code> in XML-RPC.
   */
  public static final String TRUE = "1";

  /**
   * The value of this scalar.
   */
  private Boolean value;

  /**
   * Constructor.
   */
  public XmlRpcBoolean() {
    super();
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcBoolean(Boolean value) {
    this();
    this.value = value;
  }

  /**
   * Constructor.
   * 
   * @param value
   *          the new value of this scalar.
   */
  public XmlRpcBoolean(String value) {
    this();
    this.value = TRUE.equals(value) ? Boolean.TRUE : Boolean.FALSE;
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

    if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
      matchingValue = this.value;
    }
    return matchingValue;
  }
}
