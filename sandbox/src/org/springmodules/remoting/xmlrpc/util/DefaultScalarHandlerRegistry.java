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

import org.springmodules.remoting.xmlrpc.XmlRpcEntity;
import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;

/**
 * <p>
 * Default implementation of <code>{@link ScalarHandlerRegistry}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:17 $
 */
public class DefaultScalarHandlerRegistry implements ScalarHandlerRegistry {

  /**
   * Handler for Base64 values.
   */
  private static final Base64Handler BASE_64 = new Base64Handler();

  /**
   * Handler for boolean values.
   */
  private static final BooleanHandler BOOLEAN = new BooleanHandler();

  /**
   * Handler for date/time values.
   */
  private static final DateTimeHandler DATE_TIME = new DateTimeHandler();

  /**
   * Handler for double values.
   */
  private static final DoubleHandler DOUBLE = new DoubleHandler();

  /**
   * Handler for integer values.
   */
  private static final IntegerHandler INTEGER = new IntegerHandler();

  /**
   * Constructor.
   */
  public DefaultScalarHandlerRegistry() {
    super();
  }

  /**
   * @see ScalarHandlerRegistry#parse(String, String)
   */
  public Object parse(String xmlRpcEntityName, String source)
      throws XmlRpcParsingException {

    if (XmlRpcEntity.BASE_64.equals(xmlRpcEntityName)) {
      return BASE_64.parse(source);

    } else if (XmlRpcEntity.BOOLEAN.equals(xmlRpcEntityName)) {
      return BOOLEAN.parse(source);

    } else if (XmlRpcEntity.DATE_TIME.equals(xmlRpcEntityName)) {
      return DATE_TIME.parse(source);

    } else if (XmlRpcEntity.DOUBLE.equals(xmlRpcEntityName)) {
      return DOUBLE.parse(source);

    } else if (XmlRpcEntity.I4.equals(xmlRpcEntityName)) {
      return INTEGER.parse(source);

    } else if (XmlRpcEntity.INT.equals(xmlRpcEntityName)) {
      return INTEGER.parse(source);

    } else {
      throw new XmlRpcParsingException("Unexpected element '"
          + xmlRpcEntityName + "'");
    }
  }

}
