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

import java.util.HashMap;
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

/**
 * <p>
 * Stub that simulates a DOM XML element.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class DomElementStub implements Element {

  private Map attributes;

  private String nodeName;

  /**
   * Constructor.
   */
  public DomElementStub(String newNodeName) {
    super();
    attributes = new HashMap();
    nodeName = newNodeName;
  }

  /**
   * @see org.w3c.dom.Node#appendChild(org.w3c.dom.Node)
   */
  public Node appendChild(Node child) throws DOMException {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#cloneNode(boolean)
   */
  public Node cloneNode(boolean deep) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#compareDocumentPosition(org.w3c.dom.Node)
   */
  public short compareDocumentPosition(Node other) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#getAttribute(java.lang.String)
   */
  public String getAttribute(String name) {
    return (String) attributes.get(name);
  }

  /**
   * @see org.w3c.dom.Element#getAttributeNode(java.lang.String)
   */
  public Attr getAttributeNode(String name) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#getAttributeNodeNS(java.lang.String,
   *      java.lang.String)
   */
  public Attr getAttributeNodeNS(String namespaceURI, String localName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#getAttributeNS(java.lang.String, java.lang.String)
   */
  public String getAttributeNS(String namespaceURI, String localName)
      throws DOMException {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getAttributes()
   */
  public NamedNodeMap getAttributes() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getBaseURI()
   */
  public String getBaseURI() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getChildNodes()
   */
  public NodeList getChildNodes() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#getElementsByTagName(java.lang.String)
   */
  public NodeList getElementsByTagName(String name) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#getElementsByTagNameNS(java.lang.String,
   *      java.lang.String)
   */
  public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getFeature(java.lang.String, java.lang.String)
   */
  public Object getFeature(String feature, String version) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getFirstChild()
   */
  public Node getFirstChild() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getLastChild()
   */
  public Node getLastChild() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getLocalName()
   */
  public String getLocalName() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getNamespaceURI()
   */
  public String getNamespaceURI() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getNextSibling()
   */
  public Node getNextSibling() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getNodeName()
   */
  public String getNodeName() {
    return nodeName;
  }

  /**
   * @see org.w3c.dom.Node#getNodeType()
   */
  public short getNodeType() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getNodeValue()
   */
  public String getNodeValue() throws DOMException {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getOwnerDocument()
   */
  public Document getOwnerDocument() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getParentNode()
   */
  public Node getParentNode() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getPrefix()
   */
  public String getPrefix() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getPreviousSibling()
   */
  public Node getPreviousSibling() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#getSchemaTypeInfo()
   */
  public TypeInfo getSchemaTypeInfo() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#getTagName()
   */
  public String getTagName() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getTextContent()
   */
  public String getTextContent() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#getUserData(java.lang.String)
   */
  public Object getUserData(String key) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#hasAttribute(java.lang.String)
   */
  public boolean hasAttribute(String newName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#hasAttributeNS(java.lang.String, java.lang.String)
   */
  public boolean hasAttributeNS(String namespaceURI, String localName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#hasAttributes()
   */
  public boolean hasAttributes() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#hasChildNodes()
   */
  public boolean hasChildNodes() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#insertBefore(org.w3c.dom.Node, org.w3c.dom.Node)
   */
  public Node insertBefore(Node child, Node refChild) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#isDefaultNamespace(java.lang.String)
   */
  public boolean isDefaultNamespace(String namespaceURI) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#isEqualNode(org.w3c.dom.Node)
   */
  public boolean isEqualNode(Node arg) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#isSameNode(org.w3c.dom.Node)
   */
  public boolean isSameNode(Node other) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#isSupported(java.lang.String, java.lang.String)
   */
  public boolean isSupported(String feature, String version) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#lookupNamespaceURI(java.lang.String)
   */
  public String lookupNamespaceURI(String prefix) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#lookupPrefix(java.lang.String)
   */
  public String lookupPrefix(String namespaceURI) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#normalize()
   */
  public void normalize() {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#removeAttribute(java.lang.String)
   */
  public void removeAttribute(String name) {
    attributes.remove(name);
  }

  /**
   * @see org.w3c.dom.Element#removeAttributeNode(org.w3c.dom.Attr)
   */
  public Attr removeAttributeNode(Attr oldAttr) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#removeAttributeNS(java.lang.String,
   *      java.lang.String)
   */
  public void removeAttributeNS(String namespaceURI, String localName) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#removeChild(org.w3c.dom.Node)
   */
  public Node removeChild(Node oldChild) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#replaceChild(org.w3c.dom.Node, org.w3c.dom.Node)
   */
  public Node replaceChild(Node child, Node oldChild) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#setAttribute(java.lang.String, java.lang.String)
   */
  public void setAttribute(String name, String value) {
    attributes.put(name, value);
  }

  /**
   * @see org.w3c.dom.Element#setAttributeNode(org.w3c.dom.Attr)
   */
  public Attr setAttributeNode(Attr attr) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#setAttributeNodeNS(org.w3c.dom.Attr)
   */
  public Attr setAttributeNodeNS(Attr attr) throws DOMException {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#setAttributeNS(java.lang.String, java.lang.String,
   *      java.lang.String)
   */
  public void setAttributeNS(String namespaceURI, String qualifiedName,
      String newValue) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#setIdAttribute(java.lang.String, boolean)
   */
  public void setIdAttribute(String name, boolean isId) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#setIdAttributeNode(org.w3c.dom.Attr, boolean)
   */
  public void setIdAttributeNode(Attr idAttr, boolean isId) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Element#setIdAttributeNS(java.lang.String,
   *      java.lang.String, boolean)
   */
  public void setIdAttributeNS(String namespaceURI, String localName,
      boolean newIsId) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#setNodeValue(java.lang.String)
   */
  public void setNodeValue(String nodeValue) throws DOMException {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#setPrefix(java.lang.String)
   */
  public void setPrefix(String prefix) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#setTextContent(java.lang.String)
   */
  public void setTextContent(String textContent) {
    throw new UnsupportedOperationException();
  }

  /**
   * @see org.w3c.dom.Node#setUserData(java.lang.String, java.lang.Object,
   *      org.w3c.dom.UserDataHandler)
   */
  public Object setUserData(String key, Object data, UserDataHandler handler) {
    throw new UnsupportedOperationException();
  }

}
