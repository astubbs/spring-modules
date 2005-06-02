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
package org.springmodules.xmlrpc.serializer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implementation of <code>{@link XmlRpcSerializerRegistry}</code> that stores
 * the serializer in a <code>{@link List}</code>.
 * </p>
 * <p>
 * This implementation already contains serializers for:
 * <ul>
 * <li>arrays</li>
 * <li><code>{@link Boolean}</code></li>
 * <li><code>byte[]</code></li>
 * <li><code>{@link java.util.Collection}</code></li>
 * <li><code>{@link java.util.Date}</code></li>
 * <li><code>{@link Double}</code></li>
 * <li><code>{@link Float}</code></li>
 * <li><code>{@link Integer}</code></li>
 * <li><code>{@link java.util.Map}</code></li>
 * <li>primitives</li>
 * <li><code>{@link String}</code></li>
 * </ul>
 * Users only need to add their custom serializers.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.1 $ $Date: 2005/06/02 00:27:18 $
 */
public final class ListSerializerRegistry implements XmlRpcSerializerRegistry {

  /**
   * Serializers stored in this registry.
   */
  private List serializers;

  /**
   * Constructor.
   */
  public ListSerializerRegistry() {
    super();
    this.initSerializers();
  }

  /**
   * Initializes the default serializers.
   */
  private void initSerializers() {
    this.serializers = new ArrayList();

    this.serializers.add(new ArraySerializer());
    this.serializers.add(new CollectionSerializer());
    this.serializers.add(SupportedDataTypeSerializer.BOOLEAN_SERIALIZER);
    this.serializers.add(SupportedDataTypeSerializer.BYTE_ARRAY_SERIALIZER);
    this.serializers.add(SupportedDataTypeSerializer.DATE_SERIALIZER);
    this.serializers.add(SupportedDataTypeSerializer.DOUBLE_SERIALIZER);
    this.serializers.add(SupportedDataTypeSerializer.FLOAT_SERIALIZER);
    this.serializers.add(SupportedDataTypeSerializer.INTEGER_SERIALIZER);
    this.serializers.add(SupportedDataTypeSerializer.STRING_SERIALIZER);
  }

  /**
   * @see XmlRpcSerializerRegistry#findSerializer(Object)
   */
  public XmlRpcSerializer findSerializer(Object targetObject) {
    Class clazz = (targetObject == null ? String.class : targetObject
        .getClass());

    boolean found = false;
    XmlRpcSerializer serializer = null;

    int serializerCount = this.serializers.size();
    for (int i = 0; i < serializerCount; i++) {
      serializer = (XmlRpcSerializer) this.serializers.get(i);

      if (clazz.isArray()) {
        clazz = Array.class;
      }

      if (serializer.isSupported(clazz)) {
        found = true;
        break;
      }
    }

    if (!found) {
      throw new ClassNotSupportedException("The class '" + clazz.getName()
          + "' is not supported");
    }

    return serializer;
  }

}
