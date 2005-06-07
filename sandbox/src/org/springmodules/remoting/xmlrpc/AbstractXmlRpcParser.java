/* 
 * Created on Jun 5, 2005
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * <p>
 * Template for parsers of XML-RPC request or a XML-RPC response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/06/07 04:37:09 $
 */
public abstract class AbstractXmlRpcParser {

  /**
   * Represents a member of a "struct" member.
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
   * Name of the XML element representing an array.
   */
  protected static final String ARRAY = "array";

  /**
   * Name of the XML element representing a base64-encoded binary.
   */
  protected static final String BASE_64 = "base64";

  /**
   * Name of the XML element representing a boolean.
   */
  protected static final String BOOLEAN = "boolean";

  /**
   * Name of the XML element representing the data portion of an array.
   */
  protected static final String DATA = "data";

  /**
   * Format used to create a new <code>java.util.Date</code> from a XML-RPC
   * date.
   */
  protected static final String DATE_FORMAT = "yyyyMMdd'T'HH:mm:ss";

  /**
   * Name of the XML element representing a date.
   */
  protected static final String DATE_TIME = "dateTime.iso8601";

  /**
   * Name of the XML element representing a double.
   */
  protected static final String DOUBLE = "double";

  /**
   * Name of the XML element representing an integer.
   */
  protected static final String I4 = "i4";

  /**
   * Name of the XML element representing an integer.
   */
  protected static final String INT = "int";

  /**
   * Name of the XML element representing a member of a struct.
   */
  protected static final String MEMBER = "member";

  /**
   * Name of the XML element representing the name of a member of a struct.
   */
  protected static final String NAME = "name";

  /**
   * Name of the XML element representing a parameter.
   */
  protected static final String PARAM = "param";

  /**
   * Name of the XML element representing a list of parameters.
   */
  protected static final String PARAMS = "params";

  /**
   * Name of the XML element representing a string.
   */
  protected static final String STRING = "string";

  /**
   * Name of the XML element representing a struct.
   */
  protected static final String STRUCT = "struct";

  /**
   * Name of the XML element representing the value of a parameter.
   */
  protected static final String VALUE = "value";

  /**
   * Constructor.
   */
  public AbstractXmlRpcParser() {
    super();
  }

  /**
   * Creates a new <code>java.util.List</code> from the specified DOM element.
   * 
   * @param arrayElement
   *          the DOM element.
   * @return the new array of <code>java.util.List</code>s.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child. Only one "data" element
   *           is allowed inside an "array" element.
   * @see #parseDataElement(Element)
   */
  protected List parseArrayElement(Element arrayElement) {
    NodeList childNodes = arrayElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (DATA.equals(nodeName)) {
          return this.parseDataElement((Element) node);
        }
        throw new XmlRpcParsingException("Unknown entity '" + nodeName + "'");
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Creates a new array of <code>byte</code>s from the specified DOM
   * element.
   * 
   * @param base64Element
   *          the DOM element.
   * @return the new array of <code>byte</code>s.
   */
  protected byte[] parseBase64Element(Element base64Element) {
    String nodeValue = DomUtils.getTextValue(base64Element);
    byte[] byteArray = Base64.decodeBase64(nodeValue.getBytes());
    return byteArray;
  }

  /**
   * Creates a new <code>Boolean</code> from the specified DOM element.
   * 
   * @param booleanElement
   *          the DOM element.
   * @return the new <code>Boolean</code>.
   */
  protected Boolean parseBooleanElement(Element booleanElement) {
    String text = DomUtils.getTextValue(booleanElement);
    Boolean bool = "1".equals(text) ? Boolean.TRUE : Boolean.FALSE;
    return bool;
  }

  /**
   * Creates a new <code>java.util.List</code> from the specified DOM element.
   * 
   * @param dataElement
   *          the DOM element.
   * @return the new array of <code>java.util.List</code>s.
   * @see #parseDateTimeElement(Element)
   * @see #parseDoubleElement(Element)
   * @see #parseIntegerElement(Element)
   */
  protected List parseDataElement(Element dataElement) {
    List list = new ArrayList();

    NodeList childNodes = dataElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (VALUE.equals(nodeName)) {
          Object object = this.parseValueElement((Element) node);
          list.add(object);
        }
      }
    }
    return list;
  }

  /**
   * Creates a new <code>java.util.Date</code> from the specified DOM element.
   * 
   * @param dateElement
   *          the DOM element.
   * @return the new <code>java.util.Date</code>.
   * @throws XmlRpcParsingException
   *           if the value of the element cannot be parsed into a date.
   */
  protected Date parseDateTimeElement(Element dateElement) {
    String text = DomUtils.getTextValue(dateElement);
    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    Date date = null;

    try {
      date = dateFormat.parse(text);
    } catch (ParseException exception) {
      throw new XmlRpcParsingException("Could not parse date", exception);
    }

    return date;
  }

  /**
   * Creates a new <code>Double</code> from the specified DOM element.
   * 
   * @param doubleElement
   *          the DOM element.
   * @return the new <code>Double</code>.
   * @throws XmlRpcParsingException
   *           if the value of the element cannot be converted to a double.
   */
  protected Double parseDoubleElement(Element doubleElement) {
    String nodeValue = DomUtils.getTextValue(doubleElement);

    Double doubleValue = null;
    try {
      doubleValue = new Double(nodeValue);
    } catch (NumberFormatException exception) {
      throw new XmlRpcParsingException("Could not parse double", exception);
    }

    return doubleValue;
  }

  /**
   * Creates a new <code>Integer</code> from the specified DOM element.
   * 
   * @param integerElement
   *          the DOM element.
   * @return the new <code>Integer</code>.
   * @throws XmlRpcParsingException
   *           if the value of the element cannot be converted to an integer.
   */
  protected Integer parseIntegerElement(Element integerElement) {
    String nodeValue = DomUtils.getTextValue(integerElement);

    Integer intValue = null;
    try {
      intValue = new Integer(nodeValue);
    } catch (NumberFormatException exception) {
      throw new XmlRpcParsingException("Could not parse integer", exception);
    }

    return intValue;
  }

  /**
   * Creates a new <code>StructMember</code> from the specified DOM element.
   * 
   * @param memberElement
   *          the DOM element.
   * @return the new <code>StructMember</code>.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child. Only one "name" element
   *           and one "value" element are allowed inside an "member" element.
   * @see #parseValueElement(Element)
   */
  protected StructMember parseMemberElement(Element memberElement) {
    String name = null;
    Object value = null;

    NodeList childNodes = memberElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (NAME.equals(nodeName)) {
          name = DomUtils.getTextValue((Element) node);

        } else if (VALUE.equals(nodeName)) {
          value = this.parseValueElement((Element) node);

        } else {
          throw new XmlRpcParsingException("Unknown entity '" + nodeName + "'");
        }
      }
    }

    return new StructMember(name, value);
  }

  /**
   * Creates a new Object from the specified DOM element.
   * 
   * @param parameterElement
   *          the DOM element.
   * @return the new <code>StructMember</code>.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child.
   * @see #parseValueElement(Element)
   */
  protected Object parseParameterElement(Element parameterElement) {
    NodeList childNodes = parameterElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();
        if (VALUE.equals(nodeName)) {
          return this.parseValueElement((Element) node);
        }
        throw new XmlRpcParsingException("Unknown entity '" + nodeName + "'");
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Parses the given DOM element containing parameters for a XML-RPC
   * request/response.
   * 
   * @param parametersElement
   *          the DOM element.
   * @param parameterTypes
   *          a <code>List</code> that will store the class of the parsed
   *          parameters.
   * @param arguments
   *          a <code>List</code> that will store the parsed parameters.
   */
  protected void parseParametersElement(Element parametersElement,
      List parameterTypes, List arguments) {
    NodeList childNodes = parametersElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);
      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (PARAM.equals(nodeName)) {
          Object parameter = this.parseParameterElement((Element) node);

          if (parameter instanceof List) {
            parameterTypes.add(List.class);

          } else if (parameter instanceof Map) {
            parameterTypes.add(Map.class);

          } else {
            parameterTypes.add(parameter.getClass());
          }

          arguments.add(parameter);

        } else {
          throw new XmlRpcParsingException("Unknown entity '" + nodeName + "'");
        }
      }
    }
  }

  /**
   * Creates a new <code>String</code> from the specified DOM element.
   * 
   * @param stringElement
   *          the DOM element.
   * @return the new <code>String</code>.
   */
  protected String parseStringElement(Element stringElement) {
    String nodeValue = DomUtils.getTextValue(stringElement);
    return nodeValue;
  }

  /**
   * Creates a new <code>java.util.Map</code> from the specified DOM element.
   * 
   * @param structElement
   *          the DOM element.
   * @return the new array of <code>java.util.Map</code>s.
   * @see #parseMemberElement(Element)
   */
  protected Map parseStructElement(Element structElement) {
    Map map = new HashMap();

    NodeList childNodes = structElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (MEMBER.equals(nodeName)) {
          StructMember member = this.parseMemberElement((Element) node);
          map.put(member.name, member.value);
        }

      }
    }

    return map;
  }

  /**
   * Creates a new Object from the specified DOM element.
   * 
   * @param valueElement
   *          the DOM element.
   * @return the new <code>StructMember</code>.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child.
   * @see #parseArrayElement(Element)
   * @see #parseBase64Element(Element)
   * @see #parseBooleanElement(Element)
   * @see #parseDateTimeElement(Element)
   * @see #parseDoubleElement(Element)
   * @see #parseIntegerElement(Element)
   * @see #parseStringElement(Element)
   * @see #parseStructElement(Element)
   */
  protected Object parseValueElement(Element valueElement) {
    NodeList childNodes = valueElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();
        if (ARRAY.equals(nodeName)) {
          return this.parseArrayElement((Element) node);

        } else if (BASE_64.equals(nodeName)) {
          return this.parseBase64Element((Element) node);

        } else if (BOOLEAN.equals(nodeName)) {
          return this.parseBooleanElement((Element) node);

        } else if (DATE_TIME.equals(nodeName)) {
          return this.parseDateTimeElement((Element) node);

        } else if (DOUBLE.equals(nodeName)) {
          return this.parseDoubleElement((Element) node);

        } else if (I4.equalsIgnoreCase(nodeName) || INT.equals(nodeName)) {
          return this.parseIntegerElement((Element) node);

        } else if (STRING.equalsIgnoreCase(nodeName)) {
          return this.parseStringElement((Element) node);

        } else if (STRUCT.equalsIgnoreCase(nodeName)) {
          return this.parseStructElement((Element) node);

        } else {
          throw new XmlRpcParsingException("Unknown entity '" + nodeName + "'");
        }
      } else if (node instanceof Text) {
        return this.parseStringElement(valueElement);
      }
    }

    // we should not reach this point.
    return null;
  }

}
