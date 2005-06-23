/* 
 * Created on Jun 22, 2005
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
package org.springmodules.remoting.xmlrpc.dom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springmodules.remoting.xmlrpc.XmlRpcWritingException;
import org.springmodules.remoting.xmlrpc.support.XmlRpcArray;
import org.springmodules.remoting.xmlrpc.support.XmlRpcBase64;
import org.springmodules.remoting.xmlrpc.support.XmlRpcBoolean;
import org.springmodules.remoting.xmlrpc.support.XmlRpcDateTime;
import org.springmodules.remoting.xmlrpc.support.XmlRpcDouble;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.support.XmlRpcInteger;
import org.springmodules.remoting.xmlrpc.support.XmlRpcScalar;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct.XmlRpcMember;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;

/**
 * <p>
 * Template for XML-RPC request/response writes that use DOM to create XML.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/23 02:13:48 $
 */
public abstract class AbstractDomXmlRpcWriter {

  /**
   * <p>
   * Implementation of <code>org.xml.sax.ErrorHandler</code> for custom
   * handling of XML parsing errors and warnings.
   * </p>
   * <p>
   * If not set, a default <code>{@link SimpleSaxErrorHandler}</code> is used
   * that simply logs warnings using the logger instance of the view class, and
   * rethrows errors to discontinue the XML transformation.
   * </p>
   * 
   * @see org.springframework.util.xml.SimpleSaxErrorHandler
   */
  private ErrorHandler errorHandler;

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Constructor.
   */
  public AbstractDomXmlRpcWriter() {
    super();
    this.setErrorHandler(new SimpleSaxErrorHandler(this.logger));
  }

  /**
   * Creates the XML representation of the given XML-RPC array.
   * 
   * @param array
   *          the XML-RPC array.
   * @param document
   *          the XML document used to create new XML elements.
   * @return the created XML element.
   */
  protected final Element createArrayElement(XmlRpcArray array, Document document) {
    Element dataElement = document.createElement(XmlRpcElementNames.DATA);

    XmlRpcElement[] elements = array.getElements();
    int elementCount = elements.length;

    for (int i = 0; i < elementCount; i++) {
      Element valueElement = this.createValueElement(elements[i], document);
      dataElement.appendChild(valueElement);
    }

    Element arrayElement = document.createElement(XmlRpcElementNames.ARRAY);
    arrayElement.appendChild(dataElement);

    return arrayElement;
  }

  /**
   * Creates an empty XML document.
   * 
   * @return the created XML document.
   */
  protected final Document createEmptyXmlDocument() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("Using JAXP implementation [" + factory + "]");
      }

      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      docBuilder.setErrorHandler(this.errorHandler);

      return docBuilder.newDocument();

    } catch (ParserConfigurationException exception) {
      throw new XmlRpcWritingException("Parser configuration exception",
          exception);
    }
  }

  /**
   * Creates a XML representation of the given member of a XML-RPC struct.
   * 
   * @param member
   *          the member of a XML-RPC struct.
   * @param document
   *          the XML document used to create new XML elements.
   * @return the created XML element.
   */
  protected final Element createMemberElement(XmlRpcMember member, Document document) {
    Element nameElement = document.createElement(XmlRpcElementNames.NAME);
    nameElement.setTextContent(member.name);

    Element valueElement = this.createValueElement(member.value, document);

    Element memberElement = document.createElement(XmlRpcElementNames.MEMBER);
    memberElement.appendChild(nameElement);
    memberElement.appendChild(valueElement);

    return memberElement;
  }

  /**
   * Creates the XML representation of the given XML-RPC parameter.
   * 
   * @param parameter
   *          the XML-RPC parameter.
   * @param document
   *          the XML document used to create new XML elements.
   * @return the created XML element.
   */
  protected final Element createParameterElement(XmlRpcElement parameter,
      Document document) {
    Element valueElement = this.createValueElement(parameter, document);

    Element parameterElement = document.createElement(XmlRpcElementNames.PARAM);
    parameterElement.appendChild(valueElement);

    return parameterElement;
  }

  /**
   * Creates the XML representation of the given XML-RPC parameters.
   * 
   * @param parameters
   *          the XML-RPC parameters.
   * @param document
   *          the XML document used to create new XML elements.
   * @return the created XML element.
   */
  protected final Element createParametersElement(XmlRpcElement[] parameters,
      Document document) {
    Element parametersElement = document
        .createElement(XmlRpcElementNames.PARAMS);

    int parameterCount = parameters.length;
    for (int i = 0; i < parameterCount; i++) {
      Element parameterElement = this.createParameterElement(parameters[i],
          document);
      parametersElement.appendChild(parameterElement);
    }

    return parametersElement;
  }

  /**
   * Creates the XML representation of the given XML-RPC scalar value.
   * 
   * @param elementName
   *          the name of the XML element to create.
   * @param value
   *          the XML-RPC scalar value.
   * @param document
   *          the XML document used to create new XML elements.
   * @return the created XML element.
   */
  protected final Element createScalarElement(String elementName, XmlRpcScalar value,
      Document document) {
    Element scalarElement = document.createElement(elementName);
    scalarElement.setTextContent(value.getValueAsString());

    return scalarElement;
  }

  /**
   * Creates the XML representation of the given XML-RPC struct.
   * 
   * @param struct
   *          the XML-RPC struct.
   * @param document
   *          the XML document used to create new XML elements.
   * @return the created XML element.
   */
  protected final Element createStructElement(XmlRpcStruct struct, Document document) {
    Element structElement = document.createElement(XmlRpcElementNames.STRUCT);

    XmlRpcMember[] members = struct.getMembers();
    int memberCount = members.length;

    for (int i = 0; i < memberCount; i++) {
      Element memberElement = this.createMemberElement(members[i], document);
      structElement.appendChild(memberElement);
    }

    return structElement;
  }

  /**
   * Creates the XML representation of the given XML-RPC value.
   * 
   * @param value
   *          the XML-RPC value.
   * @param document
   *          the XML document used to create new XML elements.
   * @return the created XML element.
   */
  protected final Element createValueElement(XmlRpcElement value, Document document) {
    Element child = null;

    if (value instanceof XmlRpcArray) {
      child = this.createArrayElement((XmlRpcArray) value, document);

    } else if (value instanceof XmlRpcBase64) {
      child = this.createScalarElement(XmlRpcElementNames.BASE_64,
          (XmlRpcScalar) value, document);

    } else if (value instanceof XmlRpcBoolean) {
      child = this.createScalarElement(XmlRpcElementNames.BOOLEAN,
          (XmlRpcScalar) value, document);

    } else if (value instanceof XmlRpcDateTime) {
      child = this.createScalarElement(XmlRpcElementNames.DATE_TIME,
          (XmlRpcScalar) value, document);

    } else if (value instanceof XmlRpcDouble) {
      child = this.createScalarElement(XmlRpcElementNames.DOUBLE,
          (XmlRpcScalar) value, document);

    } else if (value instanceof XmlRpcInteger) {
      child = this.createScalarElement(XmlRpcElementNames.I4,
          (XmlRpcScalar) value, document);

    } else if (value instanceof XmlRpcString) {
      child = this.createScalarElement(XmlRpcElementNames.STRING,
          (XmlRpcScalar) value, document);

    } else if (value instanceof XmlRpcStruct) {
      child = this.createStructElement((XmlRpcStruct) value, document);
    }

    Element valueElement = document.createElement(XmlRpcElementNames.VALUE);
    valueElement.appendChild(child);

    return valueElement;
  }

  /**
   * Setter for the field <code>{@link #errorHandler}</code>.
   * 
   * @param errorHandler
   *          the new value to set.
   */
  public final void setErrorHandler(ErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
  }
}
