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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * <p>
 * Implementation of <code>{@link XmlRpcRequestReader}</code> that parses the
 * XML-RPC request using StAX.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/10 01:41:39 $
 */
public class StaxXmlRpcRequestReader extends AbstractStaxXmlRpcParser implements
    XmlRpcRequestReader {

  /**
   * Constructor.
   */
  public StaxXmlRpcRequestReader() {
    super();
  }

  /**
   * @see org.springmodules.remoting.xmlrpc.XmlRpcRequestReader#readXmlRpcRequest(java.io.InputStream)
   */
  public XmlRpcRemoteInvocation readXmlRpcRequest(InputStream inputStream)
      throws XmlRpcParsingException {
    String serviceAndMethodNames = null;
    XmlRpcRemoteInvocationArguments invocationArguments = null;

    XMLInputFactory factory = XMLInputFactory.newInstance();

    try {
      XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

      while (reader.hasNext()) {
        int event = reader.next();

        switch (event) {
          case XMLStreamConstants.START_ELEMENT:
            String localName = reader.getLocalName();

            if (XmlRpcEntity.METHOD_NAME.equals(localName)) {
              serviceAndMethodNames = reader.getElementText();

            } else if (XmlRpcEntity.PARAMS.equals(localName)) {
              invocationArguments = this.parseParametersElement(reader);
            }
        }
      }
      reader.close();

    } catch (XMLStreamException exception) {
      throw new XmlRpcParsingException(
          "XmlRpcParsingException parsing XML document from XML-RPC request",
          exception);

    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException ex) {
          this.logger.warn("Could not close InputStream", ex);
        }
      }
    }

    XmlRpcRemoteInvocation remoteInvocation = new XmlRpcRemoteInvocation(
        serviceAndMethodNames, invocationArguments);
    return remoteInvocation;
  }

}
