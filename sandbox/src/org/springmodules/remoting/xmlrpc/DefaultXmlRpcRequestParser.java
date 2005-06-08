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

import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Default implementation of <code>{@link XmlRpcRequestParser}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/08 01:54:07 $
 */
public class DefaultXmlRpcRequestParser extends AbstractXmlRpcParser
    implements XmlRpcRequestParser {

  /**
   * Name of the XML element representing the name of a XML-RPC method.
   */
  protected static final String METHOD_NAME = "methodName";

  /**
   * Constructor.
   */
  public DefaultXmlRpcRequestParser() {
    super();
  }

  /**
   * @see XmlRpcRequestParser#parseXmlRpcRequest(Document)
   */
  public XmlRpcRemoteInvocation parseXmlRpcRequest(Document document) {
    Element root = document.getDocumentElement();
    String beanAndMethodNames = null;
    XmlRpcRemoteInvocationArguments invocationArguments = null;

    NodeList nodeList = root.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node instanceof Element) {
        if (METHOD_NAME.equals(node.getNodeName())) {
          beanAndMethodNames = DomUtils.getTextValue((Element) node);

        } else if (PARAMS.equals(node.getNodeName())) {
          invocationArguments = this.parseParametersElement((Element) node);
        }
      }
    }

    XmlRpcRemoteInvocation remoteInvocation = new XmlRpcRemoteInvocation(
        beanAndMethodNames, invocationArguments);
    return remoteInvocation;
  }
}
