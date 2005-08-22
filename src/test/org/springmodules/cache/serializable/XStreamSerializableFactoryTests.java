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

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.cache.serializable.XStreamSerializableFactory;
import org.springmodules.cache.serializable.XStreamSerializableFactory.ObjectWrapper;

import com.thoughtworks.xstream.XStream;

import junit.framework.TestCase;

/**
 * <p>
 * Unit Tests for <code>{@link XStreamSerializableFactory}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision$ $Date$
 */
public class XStreamSerializableFactoryTests extends TestCase {

  private static Log logger = LogFactory
      .getLog(XStreamSerializableFactoryTests.class);

  /**
   * Non-serializable object.
   */
  private Puppy puppy;

  private XStreamSerializableFactory serializableFactory;

  public XStreamSerializableFactoryTests(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

    this.serializableFactory = new XStreamSerializableFactory();
    this.puppy = new Puppy("Scooby");
  }

  public void testGetOriginalValueWithArgumentBeingObjectWrapper() {
    Serializable expected = "Darth Vader";
    ObjectWrapper wrapper = new ObjectWrapper(expected);
    assertSame(expected, this.serializableFactory.getOriginalValue(wrapper));
  }

  public void testGetOriginalValueWithArgumentEqualToNull() {
    assertNull(this.serializableFactory.getOriginalValue(null));
  }

  public void testGetOriginalValueWithArgumentNotBeingObjectWrapper() {
    Object obj = "R2-D2";
    assertSame(obj, this.serializableFactory.getOriginalValue(obj));
  }

  public void testMakeSerializableIfNecessaryWithArgumentEqualToNull() {
    assertNull(this.serializableFactory.makeSerializableIfNecessary(null));
  }

  public void testMakeSerializableIfNecessaryWithNotSerializableArgument()
      throws Exception {
    XStream xstream = new XStream();
    ObjectWrapper expected = new ObjectWrapper(xstream.toXML(this.puppy));

    Object actual = this.serializableFactory
        .makeSerializableIfNecessary(this.puppy);

    logger.debug("Expected: " + expected);
    logger.debug("Actual:   " + actual);

    assertEquals(expected, actual);
    SerializationAssert.assertObjectIsSerializable(actual);
  }

  public void testMakeSerializableIfNecessaryWithSerializableArgument() {
    Object obj = "Luke Skywalker";
    Object actual = this.serializableFactory.makeSerializableIfNecessary(obj);
    assertSame(obj, actual);
  }
}
