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
 * Parses/formats <code>Boolean</code> values.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:17 $
 */
public class BooleanHandler implements ScalarHandler {

  /**
   * Represents <code>false</code> in XML-RPC.
   */
  public static final String FALSE = "0";

  /**
   * Represents <code>true</code> in XML-RPC.
   */
  public static final String TRUE = "1";

  /**
   * Constructor.
   */
  public BooleanHandler() {
    super();
  }

  /**
   * @see ScalarHandler#format(Object)
   */
  public String format(Object source) {
    Boolean booleanSource = (Boolean) source;
    return booleanSource.booleanValue() ? TRUE : FALSE;
  }

  /**
   * @see ScalarHandler#parse(String)
   */
  public Object parse(String source) {
    return TRUE.equals(source) ? Boolean.TRUE : Boolean.FALSE;
  }

}
