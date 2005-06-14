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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.xml.DomUtils;
import org.springmodules.remoting.xmlrpc.XmlRpcEntity;
import org.springmodules.remoting.xmlrpc.XmlRpcParsingException;
import org.springmodules.remoting.xmlrpc.XmlRpcRemoteInvocationArguments;
import org.springmodules.remoting.xmlrpc.util.DefaultScalarHandlerRegistry;
import org.springmodules.remoting.xmlrpc.util.ScalarHandlerRegistry;
import org.springmodules.remoting.xmlrpc.util.StructMember;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * <p>
 * Template for XML-RPC request/response parsers that use DOM.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/14 00:47:23 $
 */
public abstract class AbstractDomXmlRpcParser {

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Handles the parsing of scalar values.
   */
  private ScalarHandlerRegistry scalarHandlerRegistry;

  /**
   * Constructor.
   */
  public AbstractDomXmlRpcParser() {
    super();
    this.scalarHandlerRegistry = new DefaultScalarHandlerRegistry();
  }

  /**
   * Creates a new <code>java.util.List</code> from the specified DOM element.
   * 
   * @param arrayElement
   *          the DOM element.
   * @return the new array of <code>java.util.List</code>s.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child. Only one "data" element
   *           is allowed inside an "array" element.
   * @see #parseDataElement(Element)
   */
  protected List parseArrayElement(Element arrayElement)
      throws XmlRpcParsingException {
    NodeList childNodes = arrayElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (XmlRpcEntity.DATA.equals(nodeName)) {
          return this.parseDataElement((Element) node);
        }
        throw new XmlRpcParsingException("Unexpected element '" + nodeName
            + "'");
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Creates a new <code>java.util.List</code> from the specified DOM element.
   * 
   * @param dataElement
   *          the DOM element.
   * @return the new array of <code>java.util.List</code>s.
   * @see #parseValueElement(Element)
   */
  protected List parseDataElement(Element dataElement)
      throws XmlRpcParsingException {
    List list = new ArrayList();

    NodeList childNodes = dataElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (XmlRpcEntity.VALUE.equals(nodeName)) {
          Object object = this.parseValueElement((Element) node);
          list.add(object);
        }
      }
    }
    return list;
  }

  /**
   * Creates a new <code>StructMember</code> from the specified DOM element.
   * 
   * @param memberElement
   *          the DOM element.
   * @return the new <code>StructMember</code>.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child. Only one "name" element
   *           and one "value" element are allowed inside an "member" element.
   * @see #parseValueElement(Element)
   */
  protected StructMember parseMemberElement(Element memberElement)
      throws XmlRpcParsingException {
    String name = null;
    Object value = null;

    NodeList childNodes = memberElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (XmlRpcEntity.NAME.equals(nodeName)) {
          name = DomUtils.getTextValue((Element) node);

        } else if (XmlRpcEntity.VALUE.equals(nodeName)) {
          value = this.parseValueElement((Element) node);

        } else {
          throw new XmlRpcParsingException("Unexpected element '" + nodeName
              + "'");
        }
      }
    }

    return new StructMember(name, value);
  }

  /**
   * Creates a new Object from the specified DOM element.
   * 
   * @param parameterElement
   *          the DOM element.
   * @return the created Object.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child.
   * @see #parseValueElement(Element)
   */
  protected Object parseParameterElement(Element parameterElement)
      throws XmlRpcParsingException {
    NodeList childNodes = parameterElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();
        if (XmlRpcEntity.VALUE.equals(nodeName)) {
          return this.parseValueElement((Element) node);
        }
        throw new XmlRpcParsingException("Unexpected element '" + nodeName
            + "'");
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Parses the given DOM element containing parameters for a XML-RPC
   * request/response.
   * 
   * @param parametersElement
   *          the DOM element.
   * @return the parameters of the XML-RPC request/response.
   */
  protected XmlRpcRemoteInvocationArguments parseParametersElement(
      Element parametersElement) throws XmlRpcParsingException {
    NodeList childNodes = parametersElement.getChildNodes();
    int childCount = childNodes.getLength();

    XmlRpcRemoteInvocationArguments arguments = new XmlRpcRemoteInvocationArguments();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);
      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (XmlRpcEntity.PARAM.equals(nodeName)) {
          Object parameter = this.parseParameterElement((Element) node);
          Class parameterType = parameter.getClass();
          arguments.addArgument(parameter, parameterType);

        } else {
          throw new XmlRpcParsingException("Unexpected element '" + nodeName
              + "'");
        }
      }
    }

    return arguments;
  }

  /**
   * Creates a new <code>java.util.Map</code> from the specified DOM element.
   * 
   * @param structElement
   *          the DOM element.
   * @return the new array of <code>java.util.Map</code>s.
   * @see #parseMemberElement(Element)
   */
  protected Map parseStructElement(Element structElement)
      throws XmlRpcParsingException {
    Map map = new HashMap();

    NodeList childNodes = structElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();

        if (XmlRpcEntity.MEMBER.equals(nodeName)) {
          StructMember member = this.parseMemberElement((Element) node);
          map.put(member.name, member.value);
        }
      }
    }

    return map;
  }

  /**
   * Creates a new Object from the specified DOM element.
   * 
   * @param valueElement
   *          the DOM element.
   * @return the created Object.
   * @throws XmlRpcParsingException
   *           if the element contains an unknown child element.
   */
  protected Object parseValueElement(Element valueElement)
      throws XmlRpcParsingException {
    NodeList childNodes = valueElement.getChildNodes();
    int childCount = childNodes.getLength();

    for (int i = 0; i < childCount; i++) {
      Node node = childNodes.item(i);

      if (node instanceof Element) {
        String nodeName = node.getNodeName();
        if (XmlRpcEntity.ARRAY.equals(nodeName)) {
          return this.parseArrayElement((Element) node);

        } else if (XmlRpcEntity.STRING.equalsIgnoreCase(nodeName)) {
          return DomUtils.getTextValue((Element) node);

        } else if (XmlRpcEntity.STRUCT.equalsIgnoreCase(nodeName)) {
          return this.parseStructElement((Element) node);

        } else {
          String source = DomUtils.getTextValue((Element) node);
          return this.scalarHandlerRegistry.parse(nodeName, source);
        }
      } else if (node instanceof Text) {
        return DomUtils.getTextValue(valueElement);
      }
    }

    // we should not reach this point.
    return null;
  }

  /**
   * Setter for the field <code>{@link #scalarHandlerRegistry}</code>.
   * 
   * @param scalarHandlerRegistry the new value to set.
   */
  public final void setScalarHandlerRegistry(
      ScalarHandlerRegistry scalarHandlerRegistry) {
    this.scalarHandlerRegistry = scalarHandlerRegistry;
  }

}
