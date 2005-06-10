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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * <p>
 * Template for XML-RPC request/response parsers that use StAX.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/06/10 09:23:15 $
 */
public abstract class AbstractStaxXmlRpcParser extends AbstractXmlRpcParser {

  /**
   * Constructor.
   */
  public AbstractStaxXmlRpcParser() {
    super();
  }

  /**
   * Creates a new <code>java.util.List</code> from the current element being
   * read in the specified <code>StreamReader</code>.
   * 
   * @param reader
   *          the <code>StreamReader</code>.
   * @return the new array of <code>java.util.List</code>s.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child. Only one "data" element
   *           is allowed inside an "array" element.
   * @see #parseDataElement(XMLStreamReader)
   */
  protected List parseArrayElement(XMLStreamReader reader)
      throws XmlRpcParsingException, XMLStreamException {

    while (reader.hasNext()) {
      int event = reader.next();

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          String localName = reader.getLocalName();

          if (XmlRpcEntity.DATA.equals(localName)) {
            return this.parseDataElement(reader);
          }
          throw new XmlRpcParsingException("Parsing '" + XmlRpcEntity.ARRAY
              + "' element. Expected '" + XmlRpcEntity.DATA
              + "' element but found '" + localName + "'");
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Creates a new <code>java.util.List</code> from the current element being
   * read in the specified <code>StreamReader</code>.
   * 
   * @param reader
   *          the <code>StreamReader</code>.
   * @return the new array of <code>java.util.List</code>s.
   * @see #parseValueElement(XMLStreamReader)
   */
  protected List parseDataElement(XMLStreamReader reader)
      throws XmlRpcParsingException, XMLStreamException {
    List list = new ArrayList();

    while (reader.hasNext()) {
      int event = reader.next();
      String localName = null;

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcEntity.VALUE.equals(localName)) {
            Object object = this.parseValueElement(reader);
            list.add(object);
          }
          break;

        case XMLStreamConstants.END_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcEntity.DATA.equals(localName)
              || XmlRpcEntity.ARRAY.equals(localName)) {
            return list;
          }
      }
    }

    // we should not reach this point.
    return list;
  }

  /**
   * Creates a new Object from the current element being read in the specified
   * <code>StreamReader</code>.
   * 
   * @param reader
   *          the <code>StreamReader</code>.
   * @return the created Object.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child.
   * @see #parseValueElement(XMLStreamReader)
   */
  protected Object parseParameterElement(XMLStreamReader reader)
      throws XmlRpcParsingException, XMLStreamException {
    while (reader.hasNext()) {
      int event = reader.next();

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          String localName = reader.getLocalName();

          if (XmlRpcEntity.VALUE.equals(localName)) {
            return this.parseValueElement(reader);
          }
          throw new XmlRpcParsingException("Unexpected element '" + localName
              + "'");
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Parses the given <code>XMLStreamReader</code> containing parameters for a
   * XML-RPC request/response.
   * 
   * @param reader
   *          the <code>StreamReader</code>.
   * @return the parameters of the XML-RPC request/response.
   */
  protected XmlRpcRemoteInvocationArguments parseParametersElement(
      XMLStreamReader reader) throws XmlRpcParsingException, XMLStreamException {
    XmlRpcRemoteInvocationArguments arguments = new XmlRpcRemoteInvocationArguments();

    while (reader.hasNext()) {
      int event = reader.next();

      String localName = null;
      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcEntity.PARAM.equals(localName)) {
            Object parameter = this.parseParameterElement(reader);
            Class parameterType = this.getParameterType(parameter);
            arguments.addArgument(parameter, parameterType);

          } else {
            throw new XmlRpcParsingException("Unknown entity '" + localName
                + "'");
          }
          break;

        case XMLStreamConstants.END_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcEntity.PARAMS.equals(localName)) {
            return arguments;
          }
      }
    }

    // we should not reach this point.
    return arguments;
  }

  /**
   * Creates a new <code>java.util.Map</code> from the current element being
   * read in the specified <code>StreamReader</code>.
   * 
   * @param reader
   *          the <code>StreamReader</code>.
   * @return the new array of <code>java.util.Map</code>s.
   * @see #parseMemberElement(XMLStreamReader)
   */
  protected Map parseStructElement(XMLStreamReader reader)
      throws XmlRpcParsingException, XMLStreamException {
    Map map = new HashMap();

    while (reader.hasNext()) {
      int event = reader.next();
      String localName = null;

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcEntity.MEMBER.equals(localName)) {
            StructMember member = this.parseMemberElement(reader);
            map.put(member.name, member.value);
          }
          break;

        case XMLStreamConstants.END_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcEntity.STRUCT.equals(localName)) {
            return map;
          }
      }
    }

    // we should not reach this point.
    return map;
  }

  /**
   * Creates a new <code>StructMember</code> from the current element being
   * read in the specified <code>StreamReader</code>.
   * 
   * @param reader
   *          the <code>StreamReader</code>.
   * @return the new <code>StructMember</code>.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child. Only one "name" element
   *           and one "value" element are allowed inside an "member" element.
   * @see #parseValueElement(XMLStreamReader)
   */
  protected StructMember parseMemberElement(XMLStreamReader reader)
      throws XmlRpcParsingException, XMLStreamException {
    String name = null;
    Object value = null;

    while (reader.hasNext()) {
      int event = reader.next();
      String localName = null;

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcEntity.NAME.equals(localName)) {
            name = reader.getElementText();

          } else if (XmlRpcEntity.VALUE.equals(localName)) {
            value = this.parseValueElement(reader);

          } else {
            throw new XmlRpcParsingException("Unknown entity '" + localName
                + "'");
          }
          break;

        case XMLStreamConstants.END_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcEntity.MEMBER.equals(localName)) {
            return new StructMember(name, value);
          }
      }
    }

    // we should never reach this point.
    return new StructMember(name, value);
  }

  /**
   * Creates a new Object from the current element being read in the specified
   * <code>StreamReader</code>.
   * 
   * @param reader
   *          the <code>StreamReader</code>.
   * @return the created Object.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child.
   * @see #parseArrayElement(XMLStreamReader)
   * @see AbstractXmlRpcParser#parseBase64(String)
   * @see AbstractXmlRpcParser#parseBoolean(String)
   * @see AbstractXmlRpcParser#parseDateTime(String)
   * @see AbstractXmlRpcParser#parseDouble(String)
   * @see AbstractXmlRpcParser#parseInteger(String)
   * @see AbstractXmlRpcParser#parseString(String)
   * @see #parseStructElement(XMLStreamReader)
   */
  protected Object parseValueElement(XMLStreamReader reader)
      throws XmlRpcParsingException, XMLStreamException {

    Object value = null;

    while (reader.hasNext()) {
      int event = reader.next();
      String localName = null;

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcEntity.ARRAY.equals(localName)) {
            return this.parseArrayElement(reader);

          } else if (XmlRpcEntity.BASE_64.equals(localName)) {
            String source = reader.getElementText();
            return this.parseBase64(source);

          } else if (XmlRpcEntity.BOOLEAN.equals(localName)) {
            String source = reader.getElementText();
            return this.parseBoolean(source);

          } else if (XmlRpcEntity.DATE_TIME.equals(localName)) {
            String source = reader.getElementText();
            return this.parseDateTime(source);

          } else if (XmlRpcEntity.DOUBLE.equals(localName)) {
            String source = reader.getElementText();
            return this.parseDouble(source);

          } else if (XmlRpcEntity.I4.equalsIgnoreCase(localName)
              || XmlRpcEntity.INT.equals(localName)) {
            String source = reader.getElementText();
            return this.parseInteger(source);

          } else if (XmlRpcEntity.STRING.equalsIgnoreCase(localName)) {
            String source = reader.getElementText();
            return this.parseString(source);

          } else if (XmlRpcEntity.STRUCT.equalsIgnoreCase(localName)) {
            return this.parseStructElement(reader);

          } else {
            throw new XmlRpcParsingException("Unknown entity '" + localName
                + "'");
          }
        case XMLStreamConstants.CHARACTERS:
          String source = reader.getText();
          return this.parseString(source);
      }
    }
    return value;
  }
}
