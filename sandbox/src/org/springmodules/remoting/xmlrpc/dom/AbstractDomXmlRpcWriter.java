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
import org.springmodules.remoting.xmlrpc.XmlRpcWriterException;
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
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;

/**
 * <p>
 * Template for XML-RPC request/response writes that use DOM to create XML.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/09/25 05:20:03 $
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

  protected final Log logger = LogFactory.getLog(getClass());

  public AbstractDomXmlRpcWriter() {
    super();
    setErrorHandler(new SimpleSaxErrorHandler(logger));
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
  protected final Element createArrayElement(XmlRpcArray array,
      Document document) {
    Element dataElement = document.createElement(XmlRpcElementNames.DATA);

    XmlRpcElement[] elements = array.getElements();
    int elementCount = elements.length;

    for (int i = 0; i < elementCount; i++) {
      Element valueElement = createValueElement(elements[i], document);
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
      if (logger.isDebugEnabled()) {
        logger.debug("Using JAXP implementation [" + factory + "]");
      }

      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      docBuilder.setErrorHandler(errorHandler);

      return docBuilder.newDocument();

    } catch (ParserConfigurationException exception) {
      throw new XmlRpcWriterException("Parser configuration exception",
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
  protected final Element createMemberElement(XmlRpcMember member,
      Document document) {
    Element nameElement = document.createElement(XmlRpcElementNames.NAME);
    Text text = document.createTextNode(member.name);
    nameElement.appendChild(text);

    Element valueElement = createValueElement(member.value, document);

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
    Element valueElement = createValueElement(parameter, document);

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
      Element parameterElement = createParameterElement(parameters[i], document);
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
   *          the DOM document to add the created XML element to.
   * @return the created XML element.
   */
  protected final Element createScalarElement(String elementName,
      XmlRpcElement value, Document document) {
    Element scalarElement = document.createElement(elementName);
    XmlRpcScalar scalarValue = (XmlRpcScalar) value;
    Text text = document.createTextNode(scalarValue.getValueAsString());
    scalarElement.appendChild(text);

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
  protected final Element createStructElement(XmlRpcStruct struct,
      Document document) {
    Element structElement = document.createElement(XmlRpcElementNames.STRUCT);

    XmlRpcMember[] members = struct.getMembers();
    int memberCount = members.length;

    for (int i = 0; i < memberCount; i++) {
      Element memberElement = createMemberElement(members[i], document);
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
  protected final Element createValueElement(XmlRpcElement value,
      Document document) {
    Element child = null;

    if (value instanceof XmlRpcArray) {
      child = createArrayElement((XmlRpcArray) value, document);

    } else if (value instanceof XmlRpcBase64) {
      child = createScalarElement(XmlRpcElementNames.BASE_64, value, document);

    } else if (value instanceof XmlRpcBoolean) {
      child = createScalarElement(XmlRpcElementNames.BOOLEAN, value, document);

    } else if (value instanceof XmlRpcDateTime) {
      child = createScalarElement(XmlRpcElementNames.DATE_TIME, value, document);

    } else if (value instanceof XmlRpcDouble) {
      child = createScalarElement(XmlRpcElementNames.DOUBLE, value, document);

    } else if (value instanceof XmlRpcInteger) {
      child = createScalarElement(XmlRpcElementNames.I4, value, document);

    } else if (value instanceof XmlRpcString) {
      child = createScalarElement(XmlRpcElementNames.STRING, value, document);

    } else if (value instanceof XmlRpcStruct) {
      child = createStructElement((XmlRpcStruct) value, document);
    }

    Element valueElement = document.createElement(XmlRpcElementNames.VALUE);
    valueElement.appendChild(child);

    return valueElement;
  }

  public final void setErrorHandler(ErrorHandler newErrorHandler) {
    errorHandler = newErrorHandler;
  }
}
