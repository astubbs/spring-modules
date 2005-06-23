/* 
 * Created on Jun 17, 2005
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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.springmodules.remoting.xmlrpc.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;
import org.springmodules.remoting.xmlrpc.XmlRpcRequestParser;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;

/**
 * <p>
 * TODO Document class.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.3 $ $Date: 2005/06/23 01:46:32 $
 */
public final class StaxXmlRpcRequestParser extends AbstractStaxXmlRpcParser
    implements XmlRpcRequestParser {

  /**
   * Constructor.
   */
  public StaxXmlRpcRequestParser() {
    super();
  }

  /**
   * @see XmlRpcRequestParser#parseRequest(InputStream)
   */
  public XmlRpcRequest parseRequest(InputStream inputStream)
      throws XmlRpcParsingException {
    XmlRpcRequest request = new XmlRpcRequest();

    try {
      XMLStreamReader reader = this.loadXmlReader(inputStream);

      while (reader.hasNext()) {
        int event = reader.next();

        switch (event) {
          case XMLStreamConstants.START_ELEMENT:
            String localName = reader.getLocalName();

            if (XmlRpcElementNames.METHOD_NAME.equals(localName)) {
              String serviceAndMethodNames = reader.getElementText();
              request.setServiceAndMethodNames(serviceAndMethodNames);

            } else if (XmlRpcElementNames.PARAMS.equals(localName)) {
              XmlRpcElement[] parameters = this.parseParametersElement(reader);
              request.setParameters(parameters);
            }
        }
      }
      reader.close();
    } catch (XMLStreamException exception) {
      throw new XmlRpcParsingException(
          "XmlRpcParsingException parsing XML-RPC request", exception);

    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException ex) {
          this.logger.warn("Could not close InputStream", ex);
        }
      }
    }

    return request;
  }

}
