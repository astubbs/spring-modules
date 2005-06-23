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
package org.springmodules.remoting.xmlrpc.stax;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springmodules.remoting.xmlrpc.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;
import org.springmodules.remoting.xmlrpc.support.XmlRpcArray;
import org.springmodules.remoting.xmlrpc.support.XmlRpcBase64;
import org.springmodules.remoting.xmlrpc.support.XmlRpcBoolean;
import org.springmodules.remoting.xmlrpc.support.XmlRpcDateTime;
import org.springmodules.remoting.xmlrpc.support.XmlRpcDouble;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcInteger;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct.XmlRpcMember;

/**
 * <p>
 * Template for XML-RPC request/response parsers that use StAX.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/23 01:45:38 $
 */
public abstract class AbstractStaxXmlRpcParser {

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Constructor.
   */
  public AbstractStaxXmlRpcParser() {
    super();
  }

  /**
   * Creates a new XML stream reader from the given InputStream.
   * 
   * @param inputStream
   *          the InputStream used as source.
   * @return the created XML stream reader.
   * @throws XmlRpcParsingException
   *           if there are any errors during the parsing.
   */
  protected final XMLStreamReader loadXmlReader(InputStream inputStream)
      throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Using StAX implementation [" + factory + "]");
    }

    XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
    return reader;
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
  protected final XmlRpcArray parseArrayElement(XMLStreamReader reader)
      throws XMLStreamException {

    while (reader.hasNext()) {
      int event = reader.next();

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          String localName = reader.getLocalName();

          if (XmlRpcElementNames.DATA.equals(localName)) {
            return this.parseDataElement(reader);
          }
          throw new XmlRpcParsingException("Unexpected element '" + localName
              + "'");
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
  protected final XmlRpcArray parseDataElement(XMLStreamReader reader)
      throws XMLStreamException {
    XmlRpcArray array = new XmlRpcArray();

    while (reader.hasNext()) {
      int event = reader.next();
      String localName = null;

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcElementNames.VALUE.equals(localName)) {
            XmlRpcElement element = this.parseValueElement(reader);
            array.add(element);
          }
          break;

        case XMLStreamConstants.END_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcElementNames.DATA.equals(localName)
              || XmlRpcElementNames.ARRAY.equals(localName)) {
            return array;
          }
      }
    }

    // we should not reach this point.
    return null;
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
  protected final XmlRpcElement parseParameterElement(XMLStreamReader reader)
      throws XMLStreamException {
    while (reader.hasNext()) {
      int event = reader.next();

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          String localName = reader.getLocalName();

          if (XmlRpcElementNames.VALUE.equals(localName)) {
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
  protected final XmlRpcElement[] parseParametersElement(XMLStreamReader reader)
      throws XMLStreamException {
    List parameters = new ArrayList();

    while (reader.hasNext()) {
      int event = reader.next();

      String localName = null;
      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcElementNames.PARAM.equals(localName)) {
            XmlRpcElement parameter = this.parseParameterElement(reader);
            parameters.add(parameter);

          } else {
            throw new XmlRpcParsingException("Unknown entity '" + localName
                + "'");
          }
          break;

        case XMLStreamConstants.END_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcElementNames.PARAMS.equals(localName)) {
            return (XmlRpcElement[]) parameters
                .toArray(new XmlRpcElement[parameters.size()]);
          }
      }
    }

    // we should not reach this point.
    return null;
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
  protected final XmlRpcStruct parseStructElement(XMLStreamReader reader)
      throws XMLStreamException {
    XmlRpcStruct struct = new XmlRpcStruct();

    while (reader.hasNext()) {
      int event = reader.next();
      String localName = null;

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcElementNames.MEMBER.equals(localName)) {
            XmlRpcMember member = this.parseMemberElement(reader);
            struct.add(member);
          }
          break;

        case XMLStreamConstants.END_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcElementNames.STRUCT.equals(localName)) {
            return struct;
          }
      }
    }

    // we should not reach this point.
    return null;
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
  protected final XmlRpcMember parseMemberElement(XMLStreamReader reader)
      throws XMLStreamException {
    String name = null;
    XmlRpcElement value = null;

    while (reader.hasNext()) {
      int event = reader.next();
      String localName = null;

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcElementNames.NAME.equals(localName)) {
            name = reader.getElementText();

          } else if (XmlRpcElementNames.VALUE.equals(localName)) {
            value = this.parseValueElement(reader);

          } else {
            throw new XmlRpcParsingException("Unknown entity '" + localName
                + "'");
          }
          break;

        case XMLStreamConstants.END_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcElementNames.MEMBER.equals(localName)) {
            if (!StringUtils.hasText(name)) {
              throw new XmlRpcParsingException(
                  "The struct member should have a name");
            }

            return new XmlRpcMember(name, value);
          }
      }
    }

    // we should never reach this point.
    return null;
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
   * @see #parseStructElement(XMLStreamReader)
   */
  protected final XmlRpcElement parseValueElement(XMLStreamReader reader)
      throws XMLStreamException {

    while (reader.hasNext()) {
      int event = reader.next();
      String localName = null;

      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          localName = reader.getLocalName();

          if (XmlRpcElementNames.ARRAY.equals(localName)) {
            return this.parseArrayElement(reader);

          } else if (XmlRpcElementNames.BASE_64.equals(localName)) {
            String source = reader.getElementText();
            return new XmlRpcBase64(source);

          } else if (XmlRpcElementNames.BOOLEAN.equals(localName)) {
            String source = reader.getElementText();
            return new XmlRpcBoolean(source);

          } else if (XmlRpcElementNames.DATE_TIME.equals(localName)) {
            String source = reader.getElementText();
            return new XmlRpcDateTime(source);

          } else if (XmlRpcElementNames.DOUBLE.equals(localName)) {
            String source = reader.getElementText();
            return new XmlRpcDouble(source);

          } else if (XmlRpcElementNames.I4.equals(localName)
              || XmlRpcElementNames.INT.equals(localName)) {
            String source = reader.getElementText();
            return new XmlRpcInteger(source);

          } else if (XmlRpcElementNames.STRING.equals(localName)
              || XmlRpcElementNames.INT.equals(localName)) {
            String source = reader.getElementText();
            return new XmlRpcString(source);

          } else if (XmlRpcElementNames.STRUCT.equals(localName)) {
            return this.parseStructElement(reader);

          } else {
            throw new XmlRpcParsingException("Unexpected element '" + localName
                + "'");
          }

        case XMLStreamConstants.CHARACTERS:
          String source = reader.getText();
          return new XmlRpcString(source);
      }
    }

    // we should not reach this point.
    return null;
  }
}
