/* 
 * Created on Aug 19, 2005
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
package org.springmodules.cache.serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.springmodules.AssertExt;

import junit.framework.Assert;

/**
 * <p>
 * Assert methods that verify an object is serializable.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class SerializationAssert {

  /**
   * Asserts that the given object is serializable.
   * 
   * @param obj
   *          the object to verify
   * @throws Exception
   *           any exception thrown when serializing the given object
   */
  public static void assertIsSerializable(Object obj) throws Exception {
    AssertExt.assertInstanceOf(Serializable.class, obj);
    Object copy = copy((Serializable) obj);
    Assert.assertEquals(obj, copy);
  }

  /**
   * Makes a copy of the given object using Java serialization
   * 
   * @param oldValue
   *          the value to copy
   * @return the copy of the given value
   * @throws Exception
   *           any exception thrown when serializing the given object
   */
  public static Serializable copy(Serializable oldValue) throws Exception {
    Serializable newValue = null;

    ByteArrayInputStream oldValueInputStream = null;
    ByteArrayOutputStream oldValueOutputStream = new ByteArrayOutputStream();

    ObjectInputStream newValueInputStream = null;
    ObjectOutputStream newValueOutputStream = null;

    try {
      newValueOutputStream = new ObjectOutputStream(oldValueOutputStream);
      newValueOutputStream.writeObject(oldValue);

      byte[] oldValueAsByteArray = oldValueOutputStream.toByteArray();
      oldValueInputStream = new ByteArrayInputStream(oldValueAsByteArray);

      newValueInputStream = new ObjectInputStream(oldValueInputStream);
      newValue = (Serializable) newValueInputStream.readObject();

    } finally {
      close(newValueInputStream);
      close(newValueOutputStream);
      close(oldValueInputStream);
      close(oldValueOutputStream);
    }
    return newValue;
  }

  private static void close(Closeable closeable) {
    if (closeable == null) {
      return;
    }

    try {
      closeable.close();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

}
