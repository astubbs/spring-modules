/* 
 * Created on Sep 1, 2005
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
package org.springmodules.cache.provider.jboss;

import org.springmodules.cache.CachingModel;
import org.springmodules.util.Strings;

/**
 * <p>
 * Configuration options needed to store, retrieve and remove objects from
 * JBossCache.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class JbossCacheCachingModel implements CachingModel {

  private static final long serialVersionUID = -9019322549512783005L;

  /**
   * FQN of the node where to store cache elements.
   */
  private String node;

  public JbossCacheCachingModel() {
    super();
  }

  public JbossCacheCachingModel(String fqn) {
    this();
    setNode(fqn);
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof JbossCacheCachingModel)) {
      return false;
    }

    final JbossCacheCachingModel cachingModel = (JbossCacheCachingModel) obj;

    if (node != null ? !node.equals(cachingModel.node)
        : cachingModel.node != null) {
      return false;
    }

    return true;
  }

  public final String getNode() {
    return node;
  }

  public int hashCode() {
    int multiplier = 31;
    int hash = 17;

    hash = multiplier * hash + (node != null ? node.hashCode() : 0);

    return hash;
  }

  public final void setNode(String newNodeFqn) {
    node = newNodeFqn;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("@" + System.identityHashCode(this) + "[");
    buffer.append("nodeFqn=" + Strings.quote(node) + "]");

    return buffer.toString();
  }
}
