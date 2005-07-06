/* 
 * Created on Jul 6, 2005
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
package org.springmodules.remoting.xmlrpc.support;

/**
 * <p>
 * Creates a new XML-RPC element from a given object according to the following
 * rules: <table border="1"> <thead>
 * <tr>
 * <th>Given object</th>
 * <th>Element to create</th>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td>Array (not array of <code>byte</code>)</td>
 * <td><code>{@link XmlRpcArray}</code></td>
 * </tr>
 * <tr>
 * <td>Array of <code>byte</code></td>
 * <td><code>{@link XmlRpcBase64}</code></td>
 * </tr>
 * <tr>
 * <td><code>Boolean</code></td>
 * <td><code>{@link XmlRpcBoolean}</code></td>
 * </tr>
 * <tr>
 * <td><code>Character</code></td>
 * <td><code>{@link XmlRpcString}</code></td>
 * </tr>
 * <tr>
 * <td><code>java.util.Collection</code></td>
 * <td><code>{@link XmlRpcArray}</code></td>
 * </tr>
 * <tr>
 * <td><code>java.util.Date</code></td>
 * <td><code>{@link XmlRpcDateTime}</code></td>
 * </tr>
 * <tr>
 * <td><code>Double</code></td>
 * <td><code>{@link XmlRpcDouble}</code></td>
 * </tr>
 * <tr>
 * <td><code>Float</code></td>
 * <td><code>{@link XmlRpcDouble}</code></td>
 * </tr>
 * <tr>
 * <td><code>Integer</code></td>
 * <td><code>{@link XmlRpcInteger}</code></td>
 * </tr>
 * <tr>
 * <td><code>java.util.Map</code></td>
 * <td><code>{@link XmlRpcStruct}</code></td>
 * </tr>
 * <tr>
 * <td><code>Long</code></td>
 * <td><code>{@link XmlRpcString}</code></td>
 * </tr>
 * <tr>
 * <td><code>Long</code></td>
 * <td><code>{@link XmlRpcString}</code></td>
 * </tr>
 * <tr>
 * <td><code>Short</code></td>
 * <td><code>{@link XmlRpcInteger}</code></td>
 * </tr>
 * <tr>
 * <td><code>String</code></td>
 * <td><code>{@link XmlRpcString}</code></td>
 * </tr>
 * <tr>
 * <td><code>Other (asummed JavaBean)</code></td>
 * <td><code>{@link XmlRpcStruct}</code></td>
 * </tr>
 * </tbody> </table>
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public interface XmlRpcElementFactory {

  /**
   * Creates a new XML-RPC element from the specified object.
   * 
   * @param source
   *          the object to create the XML-RPC element from.
   * @return the created XML-RPC element.
   */
  XmlRpcElement createXmlRpcElement(Object source);

}