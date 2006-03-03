/* 
 * Created on Mar 2, 2006
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

import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Stub that simulates a DOM XML node list.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class DomNodeListStub implements NodeList {

  private List nodes;

  /**
   * Constructor.
   * 
   * @param newNodes
   *          the nodes for this node list
   */
  public DomNodeListStub(List newNodes) {
    super();
    nodes = newNodes;
  }

  /**
   * @see org.w3c.dom.NodeList#getLength()
   */
  public int getLength() {
    return nodes.size();
  }

  /**
   * @see org.w3c.dom.NodeList#item(int)
   */
  public Node item(int index) {
    return (Node) nodes.get(index);
  }

}
