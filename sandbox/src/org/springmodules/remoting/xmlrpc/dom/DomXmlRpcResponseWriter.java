/* 
 * Created on Jun 10, 2005
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springmodules.remoting.xmlrpc.XmlRpcResponseWriter;
import org.springmodules.remoting.xmlrpc.XmlRpcWritingException;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.support.XmlRpcFault;
import org.springmodules.remoting.xmlrpc.support.XmlRpcResponse;
import org.springmodules.remoting.xmlrpc.support.XmlRpcStruct;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * Implementation of <code>{@link XmlRpcResponseWriter}</code> that uses DOM
 * for creating the XML-RPC response.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.4 $ $Date: 2005/06/23 02:13:48 $
 */
public final class DomXmlRpcResponseWriter extends AbstractDomXmlRpcWriter
    implements XmlRpcResponseWriter {

  /**
   * Constructor.
   */
  public DomXmlRpcResponseWriter() {
    super();
  }

  /**
   * Creates the XML representation of the given XML-RPC response fault.
   * 
   * @param fault
   *          the XML-RPC response fault.
   * @param document
   *          the XML document used to create new XML elements.
   * @return the created XML element.
   */
  protected Element createFaultElement(XmlRpcFault fault, Document document) {
    XmlRpcStruct struct = fault.getFaultStruct();
    Element structElement = this.createStructElement(struct, document);

    Element faultElement = document.createElement(XmlRpcElementNames.FAULT);
    faultElement.appendChild(structElement);

    return faultElement;
  }

  protected Element createMethodResponseElement(XmlRpcResponse response,
      Document document) {

    Element content = null;
    if (response.isFaultThrown()) {
      content = this.createFaultElement(response.getFault(), document);
    } else {
      content = this
          .createParametersElement(response.getParameters(), document);
    }

    Element methodResponseElement = document
        .createElement(XmlRpcElementNames.METHOD_RESPONSE);
    methodResponseElement.appendChild(content);

    return methodResponseElement;
  }

  /**
   * 
   * @see XmlRpcResponseWriter#writeResponse(XmlRpcResponse)
   */
  public byte[] writeResponse(XmlRpcResponse response) {
    Document document = this.createEmptyXmlDocument();
    Element methodResponseElement = this.createMethodResponseElement(response,
        document);
    document.appendChild(methodResponseElement);

    Source source = new DOMSource(document);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Result xmlStreamResult = new StreamResult(outputStream);

    byte[] serialized = null;
    try {
      Transformer transformer = TransformerFactory.newInstance()
          .newTransformer();
      transformer.transform(source, xmlStreamResult);
      serialized = outputStream.toByteArray();

    } catch (TransformerConfigurationException exception) {
      throw new XmlRpcWritingException("Transformer configuration exception",
          exception);

    } catch (TransformerException exception) {
      throw new XmlRpcWritingException("Transformer exception", exception);

    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException exception) {
          this.logger.warn("Could not close InputStream", exception);
        }
      }
    }

    return serialized;
  }

}
