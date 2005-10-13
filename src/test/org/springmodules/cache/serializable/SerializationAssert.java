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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Assert;

/**
 * <p>
 * Assert methods that verify an object is serializable.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class SerializationAssert {

  public static void assertObjectIsSerializable(Object obj) throws Exception {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
        byteArrayOutputStream);
    objectOutputStream.writeObject(obj);
    objectOutputStream.close();
    byte[] serialized = byteArrayOutputStream.toByteArray();

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        serialized);
    ObjectInputStream objectInputStream = new ObjectInputStream(
        byteArrayInputStream);
    Object deserialized = objectInputStream.readObject();

    Assert.assertEquals(obj, deserialized);
  }

}
