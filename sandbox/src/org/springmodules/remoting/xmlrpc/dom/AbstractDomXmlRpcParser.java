/* 
 * Created on Jun 15, 2005
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springmodules.remoting.xmlrpc.XmlRpcInternalException;
import org.springmodules.remoting.xmlrpc.XmlRpcInvalidPayloadException;
import org.springmodules.remoting.xmlrpc.XmlRpcNotWellFormedException;
import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;
import org.springmodules.remoting.xmlrpc.XmlRpcServerException;
import org.springmodules.remoting.xmlrpc.support.XmlRpcArray;
import org.springmodules.remoting.xmlrpc.support.XmlRpcBase64;
import org.springmodules.remoting.xmlrpc.support.XmlRpcBoolean;
import org.springmodules.remoting.xmlrpc.support.XmlRpcDateTime;
import org.springmodules.remoting.xmlrpc.support.XmlRpcDouble;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.support.XmlRpcInteger;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct.XmlRpcMember;
import org.springmodules.remoting.xmlrpc.util.XmlRpcParsingUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <p>
 * Template for XML-RPC request/response parsers that use DOM.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.8 $ $Date: 2005/09/25 05:20:03 $
 */
public abstract class AbstractDomXmlRpcParser {

  /**
   * SAX entity resolver to be used for parsing. By default,
   * <code>{@link XmlRpcDtdResolver}</code> will be used.
   */
  private EntityResolver entityResolver;

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

  /**
   * Flag that indicates if the XML parser should validate the XML-RPC request.
   * Default is <code>false</code>.
   */
  private boolean validating;

  public AbstractDomXmlRpcParser() {
    super();
    setEntityResolver(new XmlRpcDtdResolver());
    setErrorHandler(new SimpleSaxErrorHandler(logger));
  }

  /**
   * Creates a new XML document by parsing the given InputStream.
   * 
   * @param inputStream
   *          the InputStream to parse.
   * @return the created XML document.
   * @throws XmlRpcServerException
   *           if there are any internal errors.
   * @throws XmlRpcParsingException
   *           if there are any errors during the parsing.
   */
  protected final Document loadXmlDocument(InputStream inputStream) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      if (logger.isDebugEnabled()) {
        logger.debug("Using JAXP implementation [" + factory + "]");
      }
      factory.setValidating(validating);

      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      docBuilder.setErrorHandler(errorHandler);
      if (entityResolver != null) {
        docBuilder.setEntityResolver(entityResolver);
      }

      return docBuilder.parse(inputStream);

    } catch (ParserConfigurationException exception) {
      throw new XmlRpcInternalException("Parser configuration exception",
          exception);

    } catch (SAXParseException exception) {
      throw new XmlRpcNotWellFormedException("Line "
          + exception.getLineNumber() + " in XML-RPC payload is invalid",
          exception);

    } catch (SAXException exception) {
      throw new XmlRpcNotWellFormedException("XML-RPC payload is invalid",
          exception);

    } catch (IOException exception) {
      throw new XmlRpcInternalException(
          "IOException when parsing XML-RPC payload", exception);

    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException exception) {
          logger.warn("Could not close InputStream", exception);
        }
      }
    }
  }

  /**
   * Parses the given XML element that contains a XML-RPC array.
   * 
   * @param arrayElement
   *          the XML element to parse.
   * @return the created XML-RPC array.
   * @throws XmlRpcInvalidPayloadException
   *           if the element contains an unknown child. Only one "data" element
   *           is allowed inside an "array" element.
   * @see #parseDataElement(Element)
   */
  protected final XmlRpcArray parseArrayElement(Element arrayElement) {
    NodeList children = arrayElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();

        if (XmlRpcElementNames.DATA.equals(childName)) {
          Element dataElement = (Element) child;
          return parseDataElement(dataElement);
        }
        XmlRpcParsingUtils.handleUnexpectedElementFound(childName);
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Parses the given XML element that contains the data of a XML-RPC array.
   * 
   * @param dataElement
   *          the XML element to parse.
   * @return the created XML-RPC array.
   * @see #parseValueElement(Element)
   */
  protected final XmlRpcArray parseDataElement(Element dataElement) {
    XmlRpcArray array = new XmlRpcArray();

    NodeList children = dataElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();

        if (XmlRpcElementNames.VALUE.equals(childName)) {
          Element valueElement = (Element) child;
          XmlRpcElement element = parseValueElement(valueElement);
          array.add(element);
        }
      }
    }
    return array;
  }

  /**
   * Parses the given XML element that contains a member of a XML-RPC complex
   * structure.
   * 
   * @param memberElement
   *          the XML element to parse.
   * @return the created member of a XML-RPC complex structure.
   * @throws XmlRpcInvalidPayloadException
   *           if the element contains a child with an unknown name. Only one
   *           element with name "name" and one element with name "value" are
   *           allowed inside an "member" element.
   * @throws XmlRpcInvalidPayloadException
   *           if the name of the parsed struct member is empty.
   * @see #parseValueElement(Element)
   */
  protected final XmlRpcMember parseMemberElement(Element memberElement) {
    String name = null;
    XmlRpcElement value = null;

    NodeList children = memberElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();

        if (XmlRpcElementNames.NAME.equals(childName)) {
          Element nameElement = (Element) child;
          name = DomUtils.getTextValue(nameElement);

        } else if (XmlRpcElementNames.VALUE.equals(childName)) {
          Element valueElement = (Element) child;
          value = parseValueElement(valueElement);

        } else {
          XmlRpcParsingUtils.handleUnexpectedElementFound(childName);
        }
      }
    }

    if (!StringUtils.hasText(name)) {
      throw new XmlRpcInvalidPayloadException(
          "The struct member should have a name");
    }

    return new XmlRpcMember(name, value);
  }

  /**
   * Parses the given XML element that contains a single parameter of either a
   * XML-RPC request or a XML-RPC response.
   * 
   * @param parameterElement
   *          the XML element to parse.
   * @return the created parameter.
   * @throws XmlRpcInvalidPayloadException
   *           if the element contains a child with name other than a "value".
   * @see #parseValueElement(Element)
   */
  protected final XmlRpcElement parseParameterElement(Element parameterElement) {
    NodeList children = parameterElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String nodeName = child.getNodeName();
        if (XmlRpcElementNames.VALUE.equals(nodeName)) {
          Element valueElement = (Element) child;
          return parseValueElement(valueElement);
        }
        XmlRpcParsingUtils.handleUnexpectedElementFound(nodeName);
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Parses the given XML element that contains all the parameters or either a
   * XML-RPC request or a XML-RPC response.
   * 
   * @param parametersElement
   *          the XML element to parse.
   * @return the created parameters.
   * @throws XmlRpcInvalidPayloadException
   *           if there are elements other than "param".
   * @see #parseParameterElement(Element)
   */
  protected final XmlRpcElement[] parseParametersElement(
      Element parametersElement) {
    List parameters = new ArrayList();

    NodeList children = parametersElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);
      if (child instanceof Element) {
        String childName = child.getNodeName();

        if (XmlRpcElementNames.PARAM.equals(childName)) {
          Element parameterElement = (Element) child;
          XmlRpcElement parameter = this
              .parseParameterElement(parameterElement);
          parameters.add(parameter);

        } else {
          XmlRpcParsingUtils.handleUnexpectedElementFound(childName);
        }
      }
    }

    return (XmlRpcElement[]) parameters.toArray(new XmlRpcElement[parameters
        .size()]);
  }

  /**
   * Parses the given XML element that contains a XML-RPC complex structure.
   * 
   * @param structElement
   *          the XML element to parse.
   * @return the created XML-RPC complex structure.
   * @see #parseMemberElement(Element)
   */
  protected final XmlRpcStruct parseStructElement(Element structElement) {
    XmlRpcStruct struct = new XmlRpcStruct();

    NodeList children = structElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();

        if (XmlRpcElementNames.MEMBER.equals(childName)) {
          Element memberElement = (Element) child;
          XmlRpcMember member = parseMemberElement(memberElement);
          struct.add(member);
        }
      }
    }

    return struct;
  }

  /**
   * Parses the given XML element that contains the value of a parameter, a
   * struct member or an element of an array.
   * 
   * @param valueElement
   *          the XML element to parse.
   * @return the created value.
   * @throws XmlRpcInvalidPayloadException
   *           if there are invalid XML elements.
   * @see #parseArrayElement(Element)
   * @see #parseStructElement(Element)
   */
  protected final XmlRpcElement parseValueElement(Element valueElement) {
    NodeList children = valueElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();
        Element xmlElement = (Element) child;

        if (XmlRpcElementNames.ARRAY.equals(childName)) {
          return parseArrayElement(xmlElement);

        } else if (XmlRpcElementNames.BASE_64.equals(childName)) {
          String source = DomUtils.getTextValue(xmlElement);
          return new XmlRpcBase64(source);

        } else if (XmlRpcElementNames.BOOLEAN.equals(childName)) {
          String source = DomUtils.getTextValue(xmlElement);
          return new XmlRpcBoolean(source);

        } else if (XmlRpcElementNames.DATE_TIME.equals(childName)) {
          String source = DomUtils.getTextValue(xmlElement);
          return new XmlRpcDateTime(source);

        } else if (XmlRpcElementNames.DOUBLE.equals(childName)) {
          String source = DomUtils.getTextValue(xmlElement);
          return new XmlRpcDouble(source);

        } else if (XmlRpcElementNames.I4.equals(childName)
            || XmlRpcElementNames.INT.equals(childName)) {
          String source = DomUtils.getTextValue(xmlElement);
          return new XmlRpcInteger(source);

        } else if (XmlRpcElementNames.STRING.equals(childName)) {
          String source = DomUtils.getTextValue(xmlElement);
          return new XmlRpcString(source);

        } else if (XmlRpcElementNames.STRUCT.equals(childName)) {
          return parseStructElement(xmlElement);

        } else {
          XmlRpcParsingUtils.handleUnexpectedElementFound(childName);
        }

      } else if (child instanceof Text) {
        String source = DomUtils.getTextValue(valueElement);
        return new XmlRpcString(source);
      }
    }

    // we should not reach this point.
    return null;
  }

  public final void setEntityResolver(EntityResolver newEntityResolver) {
    entityResolver = newEntityResolver;
  }

  public final void setErrorHandler(ErrorHandler newErrorHandler) {
    errorHandler = newErrorHandler;
  }

  public final void setValidating(boolean newValidating) {
    validating = newValidating;
  }
}
