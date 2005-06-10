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
package org.springmodules.remoting.xmlrpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.xml.DomUtils;
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
 * @version $Revision: 1.1 $ $Date: 2005/06/10 01:41:40 $
 */
public abstract class AbstractDomXmlRpcParser extends AbstractXmlRpcParser {

  /**
   * Constructor.
   */
  public AbstractDomXmlRpcParser() {
    super();
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
          Class parameterType = this.getParameterType(parameter);
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
   *           if the element contains an unknown child.
   * @see #parseArrayElement(Element)
   * @see AbstractXmlRpcParser#parseBase64(String)
   * @see AbstractXmlRpcParser#parseBoolean(String)
   * @see AbstractXmlRpcParser#parseDateTime(String)
   * @see AbstractXmlRpcParser#parseDouble(String)
   * @see AbstractXmlRpcParser#parseInteger(String)
   * @see AbstractXmlRpcParser#parseString(String)
   * @see #parseStructElement(Element)
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

        } else if (XmlRpcEntity.BASE_64.equals(nodeName)) {
          String source = DomUtils.getTextValue((Element) node);
          return this.parseBase64(source);

        } else if (XmlRpcEntity.BOOLEAN.equals(nodeName)) {
          String source = DomUtils.getTextValue((Element) node);
          return this.parseBoolean(source);

        } else if (XmlRpcEntity.DATE_TIME.equals(nodeName)) {
          String source = DomUtils.getTextValue((Element) node);
          return this.parseDateTime(source);

        } else if (XmlRpcEntity.DOUBLE.equals(nodeName)) {
          String source = DomUtils.getTextValue((Element) node);
          return this.parseDouble(source);

        } else if (XmlRpcEntity.I4.equalsIgnoreCase(nodeName)
            || XmlRpcEntity.INT.equals(nodeName)) {
          String source = DomUtils.getTextValue((Element) node);
          return this.parseInteger(source);

        } else if (XmlRpcEntity.STRING.equalsIgnoreCase(nodeName)) {
          String source = DomUtils.getTextValue((Element) node);
          return this.parseString(source);

        } else if (XmlRpcEntity.STRUCT.equalsIgnoreCase(nodeName)) {
          return this.parseStructElement((Element) node);

        } else {
          throw new XmlRpcParsingException("Unknown entity '" + nodeName + "'");
        }
      } else if (node instanceof Text) {
        String source = DomUtils.getTextValue(valueElement);
        return this.parseString(source);
      }
    }

    // we should not reach this point.
    return null;
  }

}
