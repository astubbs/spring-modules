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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springmodules.remoting.xmlrpc.XmlRpcResponseWriter;
import org.w3c.dom.Document;

/**
 * <p>
 * Implementation of <code>{@link XmlRpcResponseWriter}</code> that uses DOM
 * to create t.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:23 $
 */
public class DomXmlRpcResponseWriter implements XmlRpcResponseWriter {

  /**
   * Constructor.
   */
  public DomXmlRpcResponseWriter() {
    super();
  }

  /**
   * @see XmlRpcResponseWriter#writeResponse(java.lang.Object)
   */
  public byte[] writeResponse(Object result) {
    Document document = null;

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.newDocument();

    } catch (ParserConfigurationException exception) {
      // TODO: Handle exception
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    Source source = new DOMSource(document);
    Result xmlResult = new StreamResult(outputStream);

    byte[] serialized = null;
    try {
      Transformer transformer = TransformerFactory.newInstance()
          .newTransformer();
      transformer.transform(source, xmlResult);
      serialized = outputStream.toByteArray();

    } catch (TransformerConfigurationException exception) {
      // TODO: Handle exception

    } catch (TransformerException exception) {
      // TODO: Handle exception

    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        }
        catch (IOException exception) {
          // TODO: Handle exception
          
        }
      }
    }

    return serialized;
  }

}
