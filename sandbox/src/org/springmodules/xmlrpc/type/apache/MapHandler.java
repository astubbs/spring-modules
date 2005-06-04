/* 
 * Created on Jun 3, 2005
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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.springmodules.xmlrpc.type.XmlRpcTypeHandler;
import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;

/**
 * <p>
 * Creates a new <code>{@link Hashtable}</code> containing each of the
 * elements of the given <code>{@link Map}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/04 01:16:53 $
 */
public class MapHandler extends AbstractApacheXmlRpcTypeHandler {

  /**
   * Constructor.
   */
  public MapHandler() {
    super();
  }

  /**
   * @see AbstractApacheXmlRpcTypeHandler#handle(java.lang.Object,
   *      XmlRpcTypeHandlerRegistry)
   */
  protected Object handle(Object obj, XmlRpcTypeHandlerRegistry registry) {
    Hashtable hashtable = new Hashtable();

    Map map = (Map) obj;
    Iterator entrySetIterator = map.entrySet().iterator();
    while (entrySetIterator.hasNext()) {
      Map.Entry entry = (Map.Entry) entrySetIterator.next();

      String newKey = entry.getKey().toString();

      Object value = entry.getValue();
      XmlRpcTypeHandler typeHandler = registry.findTypeHandler(value);
      Object newValue = typeHandler.handleType(value, registry);

      hashtable.put(newKey, newValue);
    }

    return hashtable;
  }

  /**
   * @see org.springmodules.xmlrpc.type.XmlRpcTypeHandler#getSupportedClass()
   */
  public Class getSupportedClass() {
    return Map.class;
  }

}
