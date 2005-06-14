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
 * @version $Revision: 1.2 $ $Date: 2005/06/14 08:59:58 $
 */
public class DefaultScalarHandlerRegistry implements ScalarHandlerRegistry {

  /**
   * Handler for Base64 values.
   */
  private ScalarHandler base64Handler;

  /**
   * Handler for boolean values.
   */
  private ScalarHandler booleanHandler;

  /**
   * Handler for date/time values.
   */
  private ScalarHandler dateTimeHandler;

  /**
   * Handler for double values.
   */
  private ScalarHandler doubleHandler;

  /**
   * Handler for integer values.
   */
  private ScalarHandler integerHandler;

  /**
   * Constructor.
   */
  public DefaultScalarHandlerRegistry() {
    super();
    this.setBase64Handler(new Base64Handler());
    this.setBooleanHandler(new BooleanHandler());
    this.setDateTimeHandler(new DateTimeHandler());
    this.setDoubleHandler(new DoubleHandler());
    this.setIntegerHandler(new IntegerHandler());
  }

  /**
   * @see ScalarHandlerRegistry#parse(String, String)
   */
  public Object parse(String xmlRpcEntityName, String source)
      throws XmlRpcParsingException {

    if (XmlRpcEntity.BASE_64.equals(xmlRpcEntityName)) {
      return this.base64Handler.parse(source);

    } else if (XmlRpcEntity.BOOLEAN.equals(xmlRpcEntityName)) {
      return this.booleanHandler.parse(source);

    } else if (XmlRpcEntity.DATE_TIME.equals(xmlRpcEntityName)) {
      return this.dateTimeHandler.parse(source);

    } else if (XmlRpcEntity.DOUBLE.equals(xmlRpcEntityName)) {
      return this.doubleHandler.parse(source);

    } else if (XmlRpcEntity.I4.equals(xmlRpcEntityName)) {
      return this.integerHandler.parse(source);

    } else if (XmlRpcEntity.INT.equals(xmlRpcEntityName)) {
      return this.integerHandler.parse(source);

    } else {
      throw new XmlRpcParsingException("Unexpected element '"
          + xmlRpcEntityName + "'");
    }
  }

  /**
   * Setter for the field <code>{@link #base64Handler}</code>.
   * 
   * @param base64Handler
   *          the new value to set.
   */
  public final void setBase64Handler(ScalarHandler base64Handler) {
    this.base64Handler = base64Handler;
  }

  /**
   * Setter for the field <code>{@link #booleanHandler}</code>.
   * 
   * @param booleanHandler
   *          the new value to set.
   */
  public final void setBooleanHandler(ScalarHandler booleanHandler) {
    this.booleanHandler = booleanHandler;
  }

  /**
   * Setter for the field <code>{@link #dateTimeHandler}</code>.
   * 
   * @param dateTimeHandler
   *          the new value to set.
   */
  public final void setDateTimeHandler(ScalarHandler dateTimeHandler) {
    this.dateTimeHandler = dateTimeHandler;
  }

  /**
   * Setter for the field <code>{@link #doubleHandler}</code>.
   * 
   * @param doubleHandler
   *          the new value to set.
   */
  public final void setDoubleHandler(ScalarHandler doubleHandler) {
    this.doubleHandler = doubleHandler;
  }

  /**
   * Setter for the field <code>{@link #integerHandler}</code>.
   * 
   * @param integerHandler
   *          the new value to set.
   */
  public final void setIntegerHandler(ScalarHandler integerHandler) {
    this.integerHandler = integerHandler;
  }

}
