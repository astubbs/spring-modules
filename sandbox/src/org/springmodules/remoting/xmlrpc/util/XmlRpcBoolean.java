/* 
 * Created on Jun 11, 2005
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
 * Boolean value supported by XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/13 08:54:08 $
 */
public class XmlRpcBoolean {

  /**
   * Represents <code>true</code> in XML-RPC.
   */
  public static final String TRUE = "1";

  /**
   * Represents <code>false</code> in XML-RPC.
   */
  public static final String FALSE = "0";

  /**
   * Creates a new <code>{@link Boolean}</code> from the specified source.
   * 
   * @param source
   *          the text to handle.
   * @return the <code>Boolean</code> representation of the given text.
   */
  public static Boolean toBoolean(String source) {
    return TRUE.equals(source) ? Boolean.TRUE : Boolean.FALSE;
  }

  /**
   * Returns a String representation of the given <code>{@link Boolean}</code>
   * 
   * @param source
   *          the given <code>Boolean</code>.
   * @return a String representation ofthe given <code>Boolean</code>.
   */
  public static String toString(Boolean source) {
    return source.booleanValue() ? TRUE : FALSE;
  }

  /**
   * Constructor.
   */
  public XmlRpcBoolean() {
    super();
  }

}
