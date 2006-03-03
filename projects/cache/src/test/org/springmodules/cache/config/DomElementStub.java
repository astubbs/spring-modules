/* 
 * Created on Jan 23, 2006
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
 * Copyright @2006 the original author or authors.
 */
package org.springmodules.cache.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

import org.springframework.util.ObjectUtils;

import org.springmodules.util.Objects;

/**
 * <p>
 * Stub that simulates a DOM XML element.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class DomElementStub implements Element {

  private Map attributes;

  private List children;

  private String nodeName;

  /**
   * Constructor.
   */
  public DomElementStub(String newNodeName) {
    super();
    attributes = new HashMap();
    children = new ArrayList();
    nodeName = newNodeName;
  }

  /**
   * @see Node#appendChild(Node)
   */
  public Node appendChild(Node child) throws DOMException {
    children.add(child);
    return child;
  }

  /**
   * @see Node#cloneNode(boolean)
   */
  public Node cloneNode(boolean deep) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#compareDocumentPosition(Node)
   */
  public short compareDocumentPosition(Node other) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DomElementStub)) {
      return false;
    }

    DomElementStub other = (DomElementStub) obj;

    if (!ObjectUtils.nullSafeEquals(nodeName, other.nodeName)) {
      return false;
    }
    if (!ObjectUtils.nullSafeEquals(attributes, other.attributes)) {
      return false;
    }
    if (!ObjectUtils.nullSafeEquals(children, other.children)) {
      return false;
    }

    return true;
  }

  /**
   * @see Element#getAttribute(String)
   */
  public String getAttribute(String name) {
    return (String) attributes.get(name);
  }

  /**
   * @see Element#getAttributeNode(String)
   */
  public Attr getAttributeNode(String name) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#getAttributeNodeNS(String, String)
   */
  public Attr getAttributeNodeNS(String namespaceURI, String localName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#getAttributeNS(String, String)
   */
  public String getAttributeNS(String namespaceURI, String localName)
      throws DOMException {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getAttributes()
   */
  public NamedNodeMap getAttributes() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getBaseURI()
   */
  public String getBaseURI() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getChildNodes()
   */
  public NodeList getChildNodes() {
    return new DomNodeListStub(children);
  }

  /**
   * @see Element#getElementsByTagName(String)
   */
  public NodeList getElementsByTagName(String name) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#getElementsByTagNameNS(String, String)
   */
  public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getFeature(String, String)
   */
  public Object getFeature(String feature, String version) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getFirstChild()
   */
  public Node getFirstChild() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getLastChild()
   */
  public Node getLastChild() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getLocalName()
   */
  public String getLocalName() {
    return nodeName;
  }

  /**
   * @see Node#getNamespaceURI()
   */
  public String getNamespaceURI() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getNextSibling()
   */
  public Node getNextSibling() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getNodeName()
   */
  public String getNodeName() {
    return nodeName;
  }

  /**
   * @see Node#getNodeType()
   */
  public short getNodeType() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getNodeValue()
   */
  public String getNodeValue() throws DOMException {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getOwnerDocument()
   */
  public Document getOwnerDocument() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getParentNode()
   */
  public Node getParentNode() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getPrefix()
   */
  public String getPrefix() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getPreviousSibling()
   */
  public Node getPreviousSibling() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#getSchemaTypeInfo()
   */
  public TypeInfo getSchemaTypeInfo() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#getTagName()
   */
  public String getTagName() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getTextContent()
   */
  public String getTextContent() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#getUserData(String)
   */
  public Object getUserData(String key) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#hasAttribute(String)
   */
  public boolean hasAttribute(String newName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#hasAttributeNS(String, String)
   */
  public boolean hasAttributeNS(String namespaceURI, String localName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#hasAttributes()
   */
  public boolean hasAttributes() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#hasChildNodes()
   */
  public boolean hasChildNodes() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    int multiplier = 31;
    int hash = 7;
    hash = multiplier * hash + (Objects.nullSafeHashCode(attributes));
    hash = multiplier * hash + (Objects.nullSafeHashCode(children));
    hash = multiplier * hash + (Objects.nullSafeHashCode(nodeName));
    return hash;
  }

  /**
   * @see Node#insertBefore(Node, Node)
   */
  public Node insertBefore(Node child, Node refChild) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#isDefaultNamespace(String)
   */
  public boolean isDefaultNamespace(String namespaceURI) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#isEqualNode(Node)
   */
  public boolean isEqualNode(Node arg) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#isSameNode(Node)
   */
  public boolean isSameNode(Node other) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#isSupported(String, String)
   */
  public boolean isSupported(String feature, String version) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#lookupNamespaceURI(String)
   */
  public String lookupNamespaceURI(String prefix) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#lookupPrefix(String)
   */
  public String lookupPrefix(String namespaceURI) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#normalize()
   */
  public void normalize() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#removeAttribute(String)
   */
  public void removeAttribute(String name) {
    attributes.remove(name);
  }

  /**
   * @see Element#removeAttributeNode(Attr)
   */
  public Attr removeAttributeNode(Attr oldAttr) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#removeAttributeNS(String, String)
   */
  public void removeAttributeNS(String namespaceURI, String localName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#removeChild(Node)
   */
  public Node removeChild(Node oldChild) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#replaceChild(Node, Node)
   */
  public Node replaceChild(Node child, Node oldChild) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#setAttribute(String, String)
   */
  public void setAttribute(String name, String value) {
    attributes.put(name, value);
  }

  /**
   * @see Element#setAttributeNode(Attr)
   */
  public Attr setAttributeNode(Attr attr) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#setAttributeNodeNS(Attr)
   */
  public Attr setAttributeNodeNS(Attr attr) throws DOMException {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#setAttributeNS(String, String, String)
   */
  public void setAttributeNS(String namespaceURI, String qualifiedName,
      String newValue) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#setIdAttribute(String, boolean)
   */
  public void setIdAttribute(String name, boolean isId) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#setIdAttributeNode(Attr, boolean)
   */
  public void setIdAttributeNode(Attr idAttr, boolean isId) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Element#setIdAttributeNS(String, String, boolean)
   */
  public void setIdAttributeNS(String namespaceURI, String localName,
      boolean newIsId) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#setNodeValue(String)
   */
  public void setNodeValue(String nodeValue) throws DOMException {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#setPrefix(String)
   */
  public void setPrefix(String prefix) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#setTextContent(String)
   */
  public void setTextContent(String textContent) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see Node#setUserData(String, Object, UserDataHandler)
   */
  public Object setUserData(String key, Object data, UserDataHandler handler) {
    throw new UnsupportedOperationException();
  }

}
