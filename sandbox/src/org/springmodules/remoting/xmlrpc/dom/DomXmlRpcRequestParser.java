/* 
 * Created on Jun 5, 2005
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

import java.io.InputStream;

import org.springframework.util.xml.DomUtils;
import org.springmodules.remoting.xmlrpc.XmlRpcInvalidPayloadException;
import org.springmodules.remoting.xmlrpc.XmlRpcRequestParser;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElement;
import org.springmodules.remoting.xmlrpc.support.XmlRpcElementNames;
import org.springmodules.remoting.xmlrpc.support.XmlRpcRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Implementation of <code>{@link XmlRpcRequestParser}</code> that uses DOM
 * for parsing XML.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.5 $ $Date: 2005/07/04 18:42:09 $
 */
public final class DomXmlRpcRequestParser extends AbstractDomXmlRpcParser
    implements XmlRpcRequestParser {

  /**
   * Constructor.
   */
  public DomXmlRpcRequestParser() {
    super();
  }

  /**
   * @see XmlRpcRequestParser#parseRequest(InputStream)
   */
  public XmlRpcRequest parseRequest(InputStream inputStream) {
    Document document = this.loadXmlDocument(inputStream);

    XmlRpcRequest request = new XmlRpcRequest();

    Element root = document.getDocumentElement();
    NodeList children = root.getChildNodes();
    int childCount = children.getLength();

    for (int i = 0; i < childCount; i++) {
      Node child = children.item(i);

      if (child instanceof Element) {
        String childName = child.getNodeName();

        if (XmlRpcElementNames.METHOD_NAME.equals(childName)) {
          Element methodElement = (Element) child;
          String serviceAndMethodNames = DomUtils.getTextValue(methodElement);
          request.setServiceAndMethodNames(serviceAndMethodNames);

        } else if (XmlRpcElementNames.PARAMS.equals(childName)) {
          Element parametersElement = (Element) child;
          XmlRpcElement[] parameters = this
              .parseParametersElement(parametersElement);
          request.setParameters(parameters);

        } else {
          throw new XmlRpcInvalidPayloadException("Unexpected element '"
              + childName + "'");
        }
      }
    }

    return request;
  }

}
