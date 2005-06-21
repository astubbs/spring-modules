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
import org.springmodules.remoting.xmlrpc.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;
import org.springmodules.remoting.xmlrpc.support.XmlRpcArray;
import org.springmodules.remoting.xmlrpc.support.XmlRpcBase64;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcScalarFactory;
import org.springmodules.remoting.xmlrpc.support.XmlRpcScalarFactoryImpl;
import org.springmodules.remoting.xmlrpc.support.XmlRpcString;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct.XmlRpcMember;
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
 * Template for XML-RPC parsers that use DOM for parsing.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/21 02:24:30 $
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

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Flag that indicates if the XML parser should validate the XML-RPC request.
   * Default is <code>false</code>.
   */
  private boolean validating;

  /**
   * Factory of
   * <code>{@link org.springmodules.remoting.xmlrpc.support.XmlRpcScalar}</code>.
   */
  private XmlRpcScalarFactory xmlRpcScalarFactory;

  /**
   * Constructor.
   */
  public AbstractDomXmlRpcParser() {
    super();
    this.setEntityResolver(new XmlRpcDtdResolver());
    this.setErrorHandler(new SimpleSaxErrorHandler(this.logger));
    this.setXmlRpcScalarFactory(new XmlRpcScalarFactoryImpl());
  }

  /**
   * Creates a new XML document by parsing the given InputStream.
   * 
   * @param inputStream
   *          the InputStream to parse.
   * @return the created XML document.
   * @throws XmlRpcParsingException
   *           if there are any errors during the parsing.
   */
  protected Document loadXmlDocument(InputStream inputStream) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("Using JAXP implementation [" + factory + "]");
      }
      factory.setValidating(this.validating);

      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      docBuilder.setErrorHandler(this.errorHandler);
      if (this.entityResolver != null) {
        docBuilder.setEntityResolver(this.entityResolver);
      }

      return docBuilder.parse(inputStream);

    } catch (ParserConfigurationException exception) {
      throw new XmlRpcParsingException(
          "Parser configuration exception while parsing XML-RPC request",
          exception);

    } catch (SAXParseException exception) {
      throw new XmlRpcParsingException("Line " + exception.getLineNumber()
          + " in XML-RPC request is invalid", exception);

    } catch (SAXException exception) {
      throw new XmlRpcParsingException("XML-RPC request is invalid", exception);

    } catch (IOException exception) {
      throw new XmlRpcParsingException("IOException parsing XML-RPC request",
          exception);

    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException exception) {
          this.logger.warn("Could not close InputStream", exception);
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
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child. Only one "data" element
   *           is allowed inside an "array" element.
   * @see #parseDataElement(Element)
   */
  protected XmlRpcArray parseArrayElement(Element arrayElement) {
    NodeList children = arrayElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();

        if (XmlRpcElementNames.DATA.equals(childName)) {
          Element dataElement = (Element) child;
          return this.parseDataElement(dataElement);
        }
        throw new XmlRpcParsingException("Unexpected element '" + childName
            + "'");
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
  protected XmlRpcArray parseDataElement(Element dataElement) {
    XmlRpcArray array = new XmlRpcArray();

    NodeList children = dataElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();

        if (XmlRpcElementNames.VALUE.equals(childName)) {
          Element valueElement = (Element) child;
          XmlRpcElement element = this.parseValueElement(valueElement);
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
   * @throws XmlRpcParsingException
   *           if the element contains a child with an unknown name. Only one
   *           element with name "name" and one element with name "value" are
   *           allowed inside an "member" element.
   * @throws XmlRpcParsingException
   *           if the name of the parsed struct member is empty.
   * @see #parseValueElement(Element)
   */
  protected XmlRpcMember parseMemberElement(Element memberElement) {
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
          value = this.parseValueElement(valueElement);

        } else {
          throw new XmlRpcParsingException("Unexpected element '" + childName
              + "'");
        }
      }
    }

    if (!StringUtils.hasText(name)) {
      throw new XmlRpcParsingException("The struct member should have a name");
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
   * @throws XmlRpcParsingException
   *           if the element contains a child with name other than a "value".
   * @see #parseValueElement(Element)
   */
  protected XmlRpcElement parseParameterElement(Element parameterElement) {
    NodeList children = parameterElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String nodeName = child.getNodeName();
        if (XmlRpcElementNames.VALUE.equals(nodeName)) {
          Element valueElement = (Element) child;
          return this.parseValueElement(valueElement);
        }
        throw new XmlRpcParsingException("Unexpected element '" + nodeName
            + "'");
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
   * @see #parseParameterElement(Element)
   */
  protected XmlRpcElement[] parseParametersElement(Element parametersElement) {
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
          throw new XmlRpcParsingException("Unexpected element '" + childName
              + "'");
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
  protected XmlRpcStruct parseStructElement(Element structElement) {
    XmlRpcStruct struct = new XmlRpcStruct();

    NodeList children = structElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();

        if (XmlRpcElementNames.MEMBER.equals(childName)) {
          Element memberElement = (Element) child;
          XmlRpcMember member = this.parseMemberElement(memberElement);
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
   * @see #parseArrayElement(Element)
   * @see #parseStructElement(Element)
   * @see XmlRpcScalarFactory#createScalarValue(String, String)
   */
  protected XmlRpcElement parseValueElement(Element valueElement) {
    NodeList children = valueElement.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();
        Element xmlElement = (Element) child;

        if (XmlRpcElementNames.ARRAY.equals(childName)) {
          return this.parseArrayElement(xmlElement);

        } else if (XmlRpcElementNames.BASE_64.equals(childName)) {
          String source = DomUtils.getTextValue(xmlElement);
          return new XmlRpcBase64(source);

        } else if (XmlRpcElementNames.STRUCT.equals(childName)) {
          return this.parseStructElement(xmlElement);

        } else {
          String source = DomUtils.getTextValue(xmlElement);
          return this.xmlRpcScalarFactory.createScalarValue(childName, source);
        }

      } else if (child instanceof Text) {
        String source = DomUtils.getTextValue(valueElement);
        return new XmlRpcString(source);
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Setter for the field <code>{@link #entityResolver}</code>.
   * 
   * @param entityResolver
   *          the new value to set.
   */
  public final void setEntityResolver(EntityResolver entityResolver) {
    this.entityResolver = entityResolver;
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

  /**
   * Setter for the field <code>{@link #validating}</code>.
   * 
   * @param validating
   *          the new value to set.
   */
  public final void setValidating(boolean validating) {
    this.validating = validating;
  }

  /**
   * Setter for the field <code>{@link #xmlRpcScalarFactory}</code>.
   * 
   * @param xmlRpcScalarFactory
   *          the new value to set.
   */
  public final void setXmlRpcScalarFactory(
      XmlRpcScalarFactory xmlRpcScalarFactory) {
    this.xmlRpcScalarFactory = xmlRpcScalarFactory;
  }
}
