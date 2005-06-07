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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <p>
 * Default implementation of <code>{@link XmlRpcServerRequestReader}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/07 04:44:42 $
 */
public class DefaultXmlRpcServerRequestReader implements InitializingBean,
    XmlRpcServerRequestReader {

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
   * Creates a XML-RPC remote invocation from the parsed XML request.
   */
  private XmlRpcServerRequestParser parser;

  /**
   * Flag that indicates if the XML parser should validate the XML-RPC request.
   * Default is <code>false</code>.
   */
  private boolean validating;

  /**
   * Constructor.
   */
  public DefaultXmlRpcServerRequestReader() {
    super();
  }

  /**
   * <p>
   * Verifies that the properties of this object have been properly set.
   * </p>
   * <p>
   * Creates a new <code>{@link XmlRpcDtdResolver}</code> if the entity
   * resolver is <code>null</code>.
   * </p>
   * <p>
   * Creates a new <code>{@link SimpleSaxErrorHandler}</code> if the error
   * handler is <code>null</code>.
   * </p>
   * <p>
   * Creates a new <code>{@link DefaultXmlRpcServerRequestParser}</code> if
   * the XML-RPC request parser is <code>null</code>.
   * </p>
   * 
   * @see InitializingBean#afterPropertiesSet()
   */
  public final void afterPropertiesSet() throws Exception {
    if (this.entityResolver == null) {
      this.setEntityResolver(new XmlRpcDtdResolver());
    }

    if (this.errorHandler == null) {
      this.setErrorHandler(new SimpleSaxErrorHandler(this.logger));
    }

    if (this.parser == null) {
      this.setParser(new DefaultXmlRpcServerRequestParser());
    }
  }

  /**
   * Delegates to the <code>{@link XmlRpcServerRequestParser}</code> the
   * parsing of the given DOM document.
   * 
   * @param document
   *          the DOM document to parse.
   * @return the XML-RPC request created by the parser.
   */
  protected XmlRpcRemoteInvocation parseXmlRpcServerRequest(Document document) {
    return this.parser.parseXmlRpcServerRequest(document);
  }

  /**
   * @see XmlRpcServerRequestReader#readXmlRpcRequest(InputStream)
   */
  public XmlRpcRemoteInvocation readXmlRpcRequest(InputStream inputStream) {
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
      return this.parseXmlRpcServerRequest(document);

    } catch (ParserConfigurationException ex) {
      throw new BeanDefinitionStoreException(
          "Parser configuration exception parsing XML from XML-RPC request", ex);

    } catch (SAXParseException ex) {
      throw new BeanDefinitionStoreException("Line " + ex.getLineNumber()
          + " in XML document from the XML-RPC request is invalid", ex);

    } catch (SAXException ex) {
      throw new BeanDefinitionStoreException(
          "XML document from XML-RPC request is invalid", ex);

    } catch (IOException ex) {
      throw new BeanDefinitionStoreException(
          "IOException parsing XML document from XML-RPC request", ex);

    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException ex) {
          this.logger.warn("Could not close InputStream", ex);
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
   * Setter for the field <code>{@link #parser}</code>.
   * 
   * @param parser
   *          the new value to set.
   */
  public final void setParser(XmlRpcServerRequestParser parser) {
    this.parser = parser;
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
