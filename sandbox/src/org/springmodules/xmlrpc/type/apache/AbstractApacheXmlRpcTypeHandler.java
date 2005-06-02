/* 
 * Created on May 31, 2005
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

import java.lang.reflect.Array;
import java.util.Vector;

import org.springmodules.xmlrpc.type.XmlRpcTypeHandler;
import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;

/**
 * <p>
 * Handles the conversion between Java data types and the data types supported
 * by the Apache XML-RCP.
 * </p>
 * <p>
 * Apache XML-RPC supports:
 * <ul>
 * <li>Arrays of <code>byte</code></li>.
 * <li><code>{@link Boolean}</code></li>
 * <li><code>{@link java.util.Date}</code></li>
 * <li><code>{@link Double}</code></li>
 * <li><code>{@link Float}</code></li>
 * <li><code>{@link java.util.Hashtable}</code> representing JavaBeans. Each
 * entry uses the name of a JavaBean property as key and the value of a JavaBean
 * property as value.</li>
 * <li><code>{@link Integer}</code></li>
 * <li><code>{@link String}</code></li>
 * <li><code>{@link String}</code></li>
 * <li><code>{@link java.util.Vector}</code> representing an array.</li>
 * </ul>
 * </p>
 * <p>
 * This template adds support for handling arrays. Subclasses just need to
 * handle the conversion between the given class to any of the classes supported
 * by Apache XML-RPC.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 23:31:49 $
 */
public abstract class AbstractApacheXmlRpcTypeHandler implements
    XmlRpcTypeHandler {

  /**
   * Constructor.
   */
  public AbstractApacheXmlRpcTypeHandler() {
    super();
  }

  /**
   * Handles the convertion of the given object into an instance of a class
   * supported by Apache XML-RPC.
   * 
   * @param obj
   *          the object to convert.
   * @param registry
   *          a registry containing data type handlers that may assist in the
   *          convertion.
   * @return a new instance of a class supported by Apache XML-RPC.
   */
  protected abstract Object handle(Object obj,
      XmlRpcTypeHandlerRegistry registry);

  /**
   * @see XmlRpcTypeHandler#handleType(Object, XmlRpcTypeHandlerRegistry)
   */
  public final Object handleType(Object obj, XmlRpcTypeHandlerRegistry registry) {
    Object handled = null;

    if (obj == null) {
      handled = "";

    } else {
      if (obj.getClass().isArray()) {
        int arraySize = Array.getLength(obj);
        Vector vector = new Vector(arraySize);

        for (int i = 0; i < arraySize; i++) {
          Object item = Array.get(obj, i);
          Object handledItem = this.handle(item, registry);
          vector.add(handledItem);
        }

        handled = vector;
      } else {
        handled = this.handle(obj, registry);
      }
    }
    return handled;
  }

  /**
   * @see XmlRpcTypeHandler#isSupported(Class)
   */
  public final boolean isSupported(Class clazz) {
    return this.getSupportedClass().isAssignableFrom(clazz);
  }
}
