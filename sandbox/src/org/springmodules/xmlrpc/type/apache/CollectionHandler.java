/* 
 * Created on Jun 1, 2005
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
package org.springmodules.xmlrpc.type.apache;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.springmodules.xmlrpc.type.XmlRpcTypeHandler;
import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;

/**
 * <p>
 * Creates a new <code>{@link Vector}</code> containing each of the elements
 * of the given <code>{@link Collection}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:50 $
 */
public final class CollectionHandler extends AbstractApacheXmlRpcTypeHandler {

  /**
   * Constructor.
   */
  public CollectionHandler() {
    super();
  }

  /**
   * @see XmlRpcTypeHandler#getSupportedClass()
   */
  public Class getSupportedClass() {
    return Collection.class;
  }

  /**
   * @see XmlRpcTypeHandler#handleType(Object, XmlRpcTypeHandlerRegistry)
   */
  protected Object handle(Object obj, XmlRpcTypeHandlerRegistry registry) {
    Vector vector = new Vector();

    if (obj != null) {
      Collection collection = (Collection) obj;
      Iterator iterator = collection.iterator();

      while (iterator.hasNext()) {
        Object item = iterator.next();
        XmlRpcTypeHandler serializer = registry.findTypeHandler(item);

        Object covertedItem = serializer.handleType(item, registry);
        vector.add(covertedItem);
      }
    }

    return vector;
  }

}
