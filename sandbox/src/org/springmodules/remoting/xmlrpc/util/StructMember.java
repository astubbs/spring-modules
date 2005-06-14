/* 
 * Created on Jun 13, 2005
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
package org.springmodules.remoting.xmlrpc.util;

/**
 * <p>
 * Represents a member of a XML-RPC struct.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:17 $
 */
public class StructMember {

  /**
   * The name of this member.
   */
  public final String name;

  /**
   * The value of this member.
   */
  public final Object value;

  /**
   * Constructor.
   * 
   * @param name
   *          the new name of this member.
   * @param value
   *          the new value of this member.
   */
  public StructMember(String name, Object value) {
    super();
    this.name = name;
    this.value = value;
  }

}
