/* 
 * Created on Jun 4, 2005
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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.util.xml.DomUtils;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <p>
 * Implementation of <code>{@link XmlRpcRequestReader}</code> that parses the
 * XML-RPC request using DOM.
 * </p>
 * s
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/13 08:56:05 $
 */
public class DomXmlRpcRequestReader extends AbstractDomXmlRpcParser implements
    XmlRpcRequestReader {

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
   * Flag that indicates if the XML parser should validate the XML-RPC request.
   * Default is <code>false</code>.
   */
  private boolean validating;

  /**
   * Constructor.
   */
  public DomXmlRpcRequestReader() {
    super();
    this.setEntityResolver(new XmlRpcDtdResolver());
    this.setErrorHandler(new SimpleSaxErrorHandler(this.logger));
  }

  /**
   * Creates a XML-RPC remote invocation from the specified DOM document.
   * 
   * @param document
   *          the DOM document.
   * @return the created XML-RPC remote invocation.
   * @see XmlRpcRemoteInvocation#XmlRpcRemoteInvocation(String,
   *      XmlRpcRemoteInvocationArguments)
   */
  protected XmlRpcRemoteInvocation parseXmlRpcRequest(Document document)
      throws XmlRpcParsingException {
    Element root = document.getDocumentElement();
    String serviceAndMethodNames = null;
    XmlRpcRemoteInvocationArguments invocationArguments = null;

    NodeList nodeList = root.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (XmlRpcEntity.METHOD_NAME.equals(nodeName)) {
          serviceAndMethodNames = DomUtils.getTextValue((Element) node);

        } else if (XmlRpcEntity.PARAMS.equals(nodeName)) {
          invocationArguments = this.parseParametersElement((Element) node);
        }
      }
    }

    XmlRpcRemoteInvocation remoteInvocation = new XmlRpcRemoteInvocation(
        serviceAndMethodNames, invocationArguments);
    return remoteInvocation;
  }

  /**
   * @see XmlRpcRequestReader#readXmlRpcRequest(InputStream)
   */
  public XmlRpcRemoteInvocation readXmlRpcRequest(InputStream inputStream)
      throws XmlRpcParsingException {
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

      Document document = docBuilder.parse(inputStream);
      return this.parseXmlRpcRequest(document);

    } catch (ParserConfigurationException exception) {
      throw new XmlRpcParsingException(
          "Parser configuration exception while parsing XML-RPC request", exception);

    } catch (SAXParseException exception) {
      throw new XmlRpcParsingException("Line " + exception.getLineNumber()
          + " in XML document from the XML-RPC request is invalid", exception);

    } catch (SAXException exception) {
      throw new XmlRpcParsingException(
          "XML document from XML-RPC request is invalid", exception);

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

}
