/* 
 * Created on Jun 8, 2005
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
package org.springmodules.remoting.xmlrpc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.remoting.xmlrpc.util.XmlRpcBase64;
import org.springmodules.remoting.xmlrpc.util.XmlRpcBoolean;
import org.springmodules.remoting.xmlrpc.util.XmlRpcDateTime;

/**
 * <p>
 * Template for XML-RPC request/response parsers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.6 $ $Date: 2005/06/13 08:55:37 $
 */
public abstract class AbstractXmlRpcParser {

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Represents a member of a "struct".
   */
  protected static class StructMember {
    /**
     * Name of the member.
     */
    public final String name;

    /**
     * Value of the member.
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

  /**
   * Constructor.
   */
  public AbstractXmlRpcParser() {
    super();
  }

  /**
   * Returns the type of the specified parameter. If the parameter is a
   * <code>List</code> or a <code>Map</code>, <code>java.util.List</code>
   * or <code>java.util.Map</code> is returned, independently of the parameter
   * class. Otherwise returns the class returned by the method
   * <code>getClass</code>.
   * 
   * @param parameter
   *          the specified parameter.
   * @return the type of the specified parameter.
   */
  protected Class getParameterType(Object parameter) {
    Class parameterType = null;
    if (parameter instanceof List) {
      parameterType = List.class;

    } else if (parameter instanceof Map) {
      parameterType = Map.class;

    } else {
      parameterType = parameter.getClass();
    }
    return parameterType;
  }

  /**
   * Creates a new array of <code>byte</code>s from the specified text.
   * 
   * @param source
   *          the text to parse.
   * @return the new array of <code>byte</code>s.
   */
  protected byte[] parseBase64(String source) {
    return XmlRpcBase64.toByteArray(source);
  }

  /**
   * Creates a new <code>Boolean</code> from the specified text.
   * 
   * @param source
   *          the text to parse.
   * @return the new <code>Boolean</code>.
   */
  protected Boolean parseBoolean(String source) {
    return XmlRpcBoolean.toBoolean(source);
  }

  /**
   * Creates a new <code>java.util.Date</code> from the specified text.
   * 
   * @param source
   *          the text to parse.
   * @return the new <code>java.util.Date</code>.
   * @throws XmlRpcParsingException
   *           if the value of the element cannot be parsed into a date.
   */
  protected Date parseDateTime(String source) throws XmlRpcParsingException {
    return XmlRpcDateTime.toDate(source);
  }

  /**
   * Creates a new <code>Double</code> from the specified text.
   * 
   * @param source
   *          the text to parse.
   * @return the new <code>Double</code>.
   * @throws XmlRpcParsingException
   *           if the value of the element cannot be converted to a double.
   */
  protected Double parseDouble(String source) throws XmlRpcParsingException {
    Double doubleValue = null;

    try {
      doubleValue = new Double(source);
    } catch (NumberFormatException exception) {

      throw new XmlRpcParsingException("'" + source + "' is not a double",
          exception);
    }

    return doubleValue;
  }

  /**
   * Creates a new <code>Integer</code> from the specified text.
   * 
   * @param source
   *          the text to parse.
   * @return the new <code>Integer</code>.
   * @throws XmlRpcParsingException
   *           if the value of the element cannot be converted to an integer.
   */
  protected Integer parseInteger(String source) throws XmlRpcParsingException {
    Integer intValue = null;

    try {
      intValue = new Integer(source);

    } catch (NumberFormatException exception) {
      throw new XmlRpcParsingException("'" + source + "' is not an integer",
          exception);
    }

    return intValue;
  }

  /**
   * Creates a new <code>String</code> from the specified text.
   * 
   * @param source
   *          the text to parse.
   * @return the new <code>String</code>.
   */
  protected String parseString(String source) {
    return source;
  }
}
