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

import java.util.ArrayList;
import java.util.List;

import org.springmodules.xmlrpc.type.ClassNotSupportedException;
import org.springmodules.xmlrpc.type.XmlRpcTypeHandler;
import org.springmodules.xmlrpc.type.XmlRpcTypeHandlerRegistry;

/**
 * <p>
 * Implementation of <code>{@link XmlRpcTypeHandlerRegistry}</code> that
 * stores implementations of <code>{@link XmlRpcTypeHandler}</code> in a
 * <code>{@link List}</code>.
 * </p>
 * <p>
 * This implementation already contains data type handlers for:
 * <ul>
 * <li><code>{@link Boolean}</code></li>
 * <li><code>byte[]</code></li>
 * <li><code>{@link Character}</code></li>
 * <li><code>{@link java.util.Collection}</code></li>
 * <li><code>{@link java.util.Date}</code></li>
 * <li><code>{@link Double}</code></li>
 * <li><code>{@link Float}</code></li>
 * <li><code>{@link Integer}</code></li>
 * <li><code>{@link Long}</code></li>
 * <li><code>{@link java.util.Map}</code></li>
 * <li><code>{@link Short}</code></li>
 * <li><code>{@link String}</code></li>
 * </ul>
 * Users only need to add their custom data type handlers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/06/04 01:20:03 $
 */
public final class ListTypeHandlerRegistry implements XmlRpcTypeHandlerRegistry {

  /**
   * Serializers stored in this registry.
   */
  private List typeHandlers;

  /**
   * Constructor.
   */
  public ListTypeHandlerRegistry() {
    super();
    this.initTypeHandlers();
  }

  /**
   * @see XmlRpcTypeHandlerRegistry#findTypeHandler(Object)
   */
  public XmlRpcTypeHandler findTypeHandler(Object targetObject) {
    // assume is a String if targetObject is null.
    Class clazz = (targetObject == null ? String.class : targetObject
        .getClass());

    boolean found = false;
    XmlRpcTypeHandler typeHandler = null;

    int typeHandlerCount = this.typeHandlers.size();
    for (int i = 0; i < typeHandlerCount; i++) {
      typeHandler = (XmlRpcTypeHandler) this.typeHandlers.get(i);

      if (clazz.isArray()) {
        clazz = clazz.getComponentType();
      }

      if (typeHandler.isSupported(clazz)) {
        found = true;
        break;
      }
    }

    if (!found) {
      throw new ClassNotSupportedException("The class '" + clazz.getName()
          + "' is not supported");
    }

    return typeHandler;
  }

  /**
   * Initializes the default data type handlers.
   */
  private void initTypeHandlers() {
    this.typeHandlers = new ArrayList();

    this.typeHandlers.add(new CharacterHandler());
    this.typeHandlers.add(new CollectionHandler());
    this.typeHandlers.add(new MapHandler());
    this.typeHandlers.add(new LongHandler());
    this.typeHandlers.add(new ShortHandler());
    this.typeHandlers.add(SupportedDataTypeHandler.BOOLEAN_HANDLER);
    this.typeHandlers.add(SupportedDataTypeHandler.BYTE_ARRAY_HANDLER);
    this.typeHandlers.add(SupportedDataTypeHandler.DATE_HANDLER);
    this.typeHandlers.add(SupportedDataTypeHandler.DOUBLE_HANDLER);
    this.typeHandlers.add(SupportedDataTypeHandler.FLOAT_HANDLER);
    this.typeHandlers.add(SupportedDataTypeHandler.INTEGER_HANDLER);
    this.typeHandlers.add(SupportedDataTypeHandler.STRING_HANDLER);
  }

}
